package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.business.BusinessFactory;
import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.lock.PushLock;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.tunnel.TunnelTip;
import org.sanhenanli.togo.network.message.MessageQueue;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.trigger.ScheduleTrigger;
import org.sanhenanli.togo.network.tunnel.AbstractStatefulTunnel;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * datetime 2020/1/22 14:08
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTunnelPusher extends PusherIdentity implements TunnelPusher {

    protected MessageQueue queue;
    protected PushRecorder recorder;
    protected PushLock lock;
    protected Executor executor;
    protected BusinessFactory businessFactory;

    public AbstractTunnelPusher(Receiver receiver, AbstractTunnel tunnel, MessageQueue queue, PushRecorder recorder, PushLock lock, Executor executor, BusinessFactory businessFactory) {
        super(receiver, tunnel);
        this.queue = queue;
        this.recorder = recorder;
        this.lock = lock;
        this.executor = executor;
        this.businessFactory = businessFactory;
    }

    @Override
    public void start() {
        executor.execute(this::pushContinuously, true);
    }

    @Override
    public void add(Message message, boolean head) {
        List<Business> bizs = businessFactory.inferiorSubstances(message.getBiz());
        for (Business biz : bizs) {
            message.setBiz(biz);
            queue.add(receiver, message, tunnel, head);
        }
        start();
    }

    @Override
    public void reportReceipt(String id) {
        queue.reportReceipt(id);
    }

    @Override
    public void pushContinuously() {
        boolean getLock = false;
        try {
            getLock = lock.tryLock(receiver, tunnel);
            if (getLock) {
                while (true) {
                    Message message = pop();
                    if (message == null) {
                        break;
                    }
                    if (!pushContinuously(message)) {
                        break;
                    }
                }
            }
        } finally {
            if (getLock) {
                lock.unlock(receiver, tunnel);
            }
        }
    }

    protected abstract Message pop();

    protected TunnelTip push(Message message) {
        TunnelTip tunnelTip = pushByStateful(message);
        if (message.getPolicy().getTunnelPolicy().isDuplex() && tunnelTip.isOk()) {
            // wait for receipt
            tunnelTip = waitForReceipt(message);
        }
        return tunnelTip;
    }

    protected TunnelTip waitForReceipt(Message message) {
        String id = message.getId();
        long timeout = message.getPolicy().getTunnelPolicy().getTimeoutMills();
        long start = System.currentTimeMillis();
        for (long now = start, step = 100; now - start < timeout; step *= 1.1, now += step) { // todo 优化等待receipt
            if (queue.consumeReceipt(id)) {
                // find receipt
                return TunnelTip.ok();
            }
        }
        return TunnelTip.noReceipt();
    }

    protected TunnelTip pushByStateful(Message message) {
        if (message.getPolicy().getTunnelPolicy().isStateful()) {
            return pushWhenConnected(message);
        } else {
            return doPush(receiver, message);
        }
    }

    protected TunnelTip pushWhenConnected(Message message) {
        if (tunnel instanceof AbstractStatefulTunnel) {
            return doPushWhenConnected(message);
        }
        return doPush(receiver, message);
    }

    protected TunnelTip doPushWhenConnected(Message message) {
        return ((AbstractStatefulTunnel) tunnel).pushWhenConnected(receiver, message);
    }

    protected TunnelTip doPush(Receiver receiver, Message message) {
        return tunnel.push(receiver, message);
    }

    protected void markTried(Message message) {
        message.markTried();
    }

    protected void preRetry(Message message, TunnelTip tunnelTip) {
        message.getPolicy().setTunnelPolicy(message.getPolicy().getRetryPolicy().getTunnelPolicy());
        // todo retry delay factory, retry max times;
        if (message.getPolicy().getRetryPolicy().isFollowSuggestion() && validSuggestTime(tunnelTip.getSuggestTime())) {
            message.getPolicy().setTrigger(ScheduleTrigger.at(tunnelTip.getSuggestTime()));
        } else {
            message.getPolicy().setTrigger(message.getPolicy().getRetryPolicy().getTrigger());
        }
    }

    protected boolean validSuggestTime(LocalDateTime time) {
        return time != null && time.isAfter(LocalDateTime.now());
    }
}
