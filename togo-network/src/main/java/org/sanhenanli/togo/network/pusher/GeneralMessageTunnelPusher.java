package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.lock.PushLock;
import org.sanhenanli.togo.network.model.Message;
import org.sanhenanli.togo.network.model.TunnelTip;
import org.sanhenanli.togo.network.queue.MessageQueue;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.trigger.PushTrigger;
import org.sanhenanli.togo.network.trigger.ScheduleTrigger;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import lombok.EqualsAndHashCode;

/**
 * datetime 2020/1/22 15:50
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
public class GeneralMessageTunnelPusher extends AbstractTunnelPusher {

    public GeneralMessageTunnelPusher(Receiver receiver, AbstractTunnel tunnel, MessageQueue queue, PushRecorder recorder, PushLock lock, Executor executor) {
        super(receiver, tunnel, queue, recorder, lock, executor);
    }

    @Override
    protected Message pop() {
        return queue.popGeneralMessage(receiver, tunnel);
    }

    @Override
    public boolean pushContinuously(Message message) {
        PushTrigger trigger = message.getPolicy().getTrigger();
        if (trigger instanceof ScheduleTrigger) {
            executor.executeOnSchedule(() -> postPush(message), ((ScheduleTrigger) trigger).getSchedule());
            return true;
        } else {
            executor.execute(() -> postPush(message));
            return true;
        }
    }

    @Override
    public boolean postPush(Message message) {
        TunnelTip tunnelTip = push(message);
        markTried(message);
        recorder.recordAttempt(message);
        if (tunnelTip.isOk()) {
            recorder.recordSuccess(message);
        } else {
            if (message.retryable()) {
                preRetry(message, tunnelTip);
                queue.add(receiver, message, tunnel, true);
            }
            recorder.recordError(message, tunnelTip);
        }
        return true;
    }
}
