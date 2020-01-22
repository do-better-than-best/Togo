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
import org.sanhenanli.togo.network.tunnel.AbstractStatefulTunnel;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import lombok.EqualsAndHashCode;

/**
 * datetime 2020/1/22 15:48
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
public class StatefulMessageTunnelPusher extends AbstractTunnelPusher {

    public StatefulMessageTunnelPusher(Receiver receiver, AbstractTunnel tunnel, MessageQueue queue, PushRecorder recorder, PushLock lock, Executor executor, BusinessFactory businessFactory) {
        super(receiver, tunnel, queue, recorder, lock, executor, businessFactory);
    }

    @Override
    protected Message pop() {
        return queue.popStatefulMessage(receiver, tunnel);
    }

    @Override
    public boolean pushContinuously(Message message) {
        PushTrigger trigger = message.getPolicy().getTrigger();
        if (trigger instanceof ScheduleTrigger) {
            executor.executeOnSchedule(() -> postPush(message), ((ScheduleTrigger) trigger).getSchedule());
            return true;
        } else {
            if (tunnel instanceof AbstractStatefulTunnel) {
                if (((AbstractStatefulTunnel) tunnel).connected(receiver)) {
                    executor.execute(() -> postPush(message));
                    return true;
                } else {
                    return false;
                }
            } else {
                executor.execute(() -> postPush(message));
                return true;
            }
        }
    }

    @Override
    public boolean postPush(Message message) {
        TunnelTip tunnelTip = push(message);
        if (tunnelTip.isOk()) {
            markTried(message);
            recorder.recordAttempt(message);
            recorder.recordSuccess(message);
        } else if (tunnelTip.isNotConnected()) {
            queue.add(receiver, message, tunnel, true);
        } else {
            markTried(message);
            recorder.recordAttempt(message);
            if (message.retryable()) {
                preRetry(message, tunnelTip);
                queue.add(receiver, message, tunnel, true);
            }
            recorder.recordError(message, tunnelTip);
        }
        return true;
    }
}
