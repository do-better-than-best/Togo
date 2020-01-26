package org.sanhenanli.togo.network.pusher;

import lombok.EqualsAndHashCode;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * datetime 2020/1/22 14:08
 * 单人单通道消息推送器
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTunnelPusher extends PusherIdentity implements TunnelPusher {

    /**
     * 全量消息队列
     */
    protected MessageQueue queue;
    /**
     * 全局推送记录器
     */
    protected PushRecorder recorder;
    /**
     * 推送锁
     */
    protected PushLock lock;
    /**
     * 推送任务执行器
     */
    protected Executor executor;
    /**
     * 业务管理器
     */
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
        List<Business> bizs = businessFactory.substances(message.getBiz().getName());
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

    /**
     * 取出该receiver*tunnel的待推消息
     * @return 消息
     */
    protected abstract Message pop();

    /**
     * 推送一条消息
     * @param message 消息
     * @return 推送结果
     */
    protected TunnelTip push(Message message) {
        TunnelTip tunnelTip = pushByStateful(message);
        if (message.getPolicy().getTunnelPolicy().isDuplex() && tunnelTip.isOk()) {
            // wait for receipt
            tunnelTip = waitForReceipt(message);
        }
        return tunnelTip;
    }

    /**
     * 等待消息回执
     * @param message 消息
     * @return 推送结果
     */
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

    /**
     * 接收者建立通道连接时才能推送
     * @param message 消息
     * @return 推送结果
     */
    protected TunnelTip pushByStateful(Message message) {
        if (message.getPolicy().getTunnelPolicy().isStateful()) {
            return pushWhenConnected(message);
        } else {
            return doPush(message);
        }
    }

    /**
     * 接收者建立通道连接时才能推送
     * @param message 消息
     * @return 推送结果
     */
    protected TunnelTip pushWhenConnected(Message message) {
        if (tunnel instanceof AbstractStatefulTunnel) {
            return doPushWhenConnected(message);
        }
        return doPush(message);
    }

    /**
     * 接收者建立通道连接时才能执行推送
     * @param message 消息
     * @return 推送结果
     */
    protected TunnelTip doPushWhenConnected(Message message) {
        return ((AbstractStatefulTunnel) tunnel).pushWhenConnected(receiver, message);
    }

    /**
     * 执行推送
     * @param message 消息
     * @return 推送结果
     */
    protected TunnelTip doPush(Message message) {
        return tunnel.push(receiver, message);
    }

    /**
     * 记录一次尝试
     * @param message 消息
     */
    protected void markTried(Message message) {
        int tryTimes = message.markTried();
    }

    /**
     * 重试前处理消息: 修改推送策略
     * @param message 消息
     * @param tunnelTip 推送结果
     */
    protected void preRetry(Message message, TunnelTip tunnelTip) {
        message.getPolicy().setTunnelPolicy(message.getPolicy().getRetryPolicy().getTunnelPolicy());
        if (message.getPolicy().getRetryPolicy().isFollowSuggestion() && validSuggestTime(tunnelTip.getSuggestTime())) {
            message.getPolicy().setTrigger(ScheduleTrigger.at(tunnelTip.getSuggestTime()));
        } else {
            message.getPolicy().setTrigger(message.getPolicy().getRetryPolicy().getTrigger());
        }
    }

    /**
     * 判断是否是有效的建议推送时间
     * @param time 建议推送时间
     * @return true有效
     */
    protected boolean validSuggestTime(LocalDateTime time) {
        return time != null && time.isAfter(LocalDateTime.now());
    }
}
