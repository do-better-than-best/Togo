package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.business.BusinessFactory;
import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.lock.PushLock;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.receiver.ReceiverFactory;
import org.sanhenanli.togo.network.tunnel.TunnelFactory;
import org.sanhenanli.togo.network.tunnel.TunnelTip;
import org.sanhenanli.togo.network.message.MessageQueue;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.trigger.PushTrigger;
import org.sanhenanli.togo.network.trigger.ScheduleTrigger;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * datetime 2020/1/22 15:19
 *
 * @author zhouwenxiang
 */
public class OrderedMessageTunnelPusher extends AbstractTunnelPusher {

    public OrderedMessageTunnelPusher(Receiver receiver, AbstractTunnel tunnel, MessageQueue queue, PushRecorder recorder, PushLock lock, Executor executor, BusinessFactory businessFactory) {
        super(receiver, tunnel, queue, recorder, lock, executor, businessFactory);
    }

    @Override
    protected Message pop() {
        return queue.popOrderedMessage(receiver, tunnel);
    }

    @Override
    public boolean pushContinuously(Message message) {
        PushTrigger trigger = message.getPolicy().getTrigger();
        if (trigger instanceof ScheduleTrigger) {
            try { // todo 优化定时有序消息
                Thread.sleep(Duration.between(LocalDateTime.now(), ((ScheduleTrigger) trigger).getSchedule()).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return postPush(message);
    }

    @Override
    public boolean postPush(Message message) {
        TunnelTip tunnelTip = push(message);
        if (tunnelTip.isOk()) {
            markTried(message);
            recorder.recordSuccess(message);
            return true;
        } else if (tunnelTip.isNotConnected() && message.getPolicy().getTunnelPolicy().isStateful()) {
            queue.add(receiver, message, tunnel, true);
            return false;
        } else {
            markTried(message);
            if (message.retryable()) {
                recorder.recordRetry(message, tunnelTip);
                preRetry(message, tunnelTip);
                return pushContinuously(message);
            }
            recorder.recordError(message, tunnelTip);
            return true;
        }
    }

}
