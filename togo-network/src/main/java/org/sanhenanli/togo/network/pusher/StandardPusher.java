package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.lock.PushLock;
import org.sanhenanli.togo.network.model.Message;
import org.sanhenanli.togo.network.model.PusherIdentity;
import org.sanhenanli.togo.network.queue.MessageQueue;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.util.HashSet;
import java.util.Set;

/**
 * datetime 2020/1/18 21:23
 *
 * @author zhouwenxiang
 */
public class StandardPusher implements Pusher {

    private MessageQueue queue;
    private PushRecorder recorder;
    private Executor executor;
    private PushLock lock;

    public StandardPusher(MessageQueue queue, PushRecorder recorder, Executor executor, PushLock lock) {
        this.queue = queue;
        this.recorder = recorder;
        this.executor = executor;
        this.lock = lock;
    }

    @Override
    public void onStart() {
        Set<PusherIdentity> pushers = queue.pushersToTrigger();
        assemblePusher(pushers).forEach(AbstractTunnelPusher::start);
    }

    @Override
    public void add(Receiver receiver, Message message, AbstractTunnel tunnel, boolean head) {
        doAdd(receiver, message, tunnel, head);
        if (message.getPolicy().getTunnelPolicy().isOrdered()) {
            assembleOrderedMessagePusher(receiver, tunnel).start();
        } else if (message.getPolicy().getTunnelPolicy().isStateful()) {
            assembleStatefulMessagePusher(receiver, tunnel).start();
        } else {
            assembleGeneralMessagePusher(receiver, tunnel).start();
        }
    }

    @Override
    public void onConnect(AbstractTunnel tunnel, Receiver receiver) {
        assembleOrderedMessagePusher(receiver, tunnel).start();
        assembleStatefulMessagePusher(receiver, tunnel).start();
    }

    @Override
    public void reportReceipt(String messageId) {
        queue.reportReceipt(messageId);
    }

    private void doAdd(Receiver receiver, Message message, AbstractTunnel wrappedTunnel, boolean head) {
        queue.add(receiver, message, wrappedTunnel, head);
    }

    private Set<AbstractTunnelPusher> assemblePusher(Set<PusherIdentity> pusher) {
        Set<AbstractTunnelPusher> pushers = new HashSet<>();
        pusher.forEach(p -> {
            pushers.add(assembleOrderedMessagePusher(p.getReceiver(), p.getTunnel()));
            pushers.add(assembleStatefulMessagePusher(p.getReceiver(), p.getTunnel()));
            pushers.add(assembleGeneralMessagePusher(p.getReceiver(), p.getTunnel()));
        });
        return pushers;
    }

    private OrderedMessageTunnelPusher assembleOrderedMessagePusher(Receiver receiver, AbstractTunnel tunnel) {
        return new OrderedMessageTunnelPusher(receiver, tunnel, queue, recorder, lock, executor);
    }

    private StatefulMessageTunnelPusher assembleStatefulMessagePusher(Receiver receiver, AbstractTunnel tunnel) {
        return new StatefulMessageTunnelPusher(receiver, tunnel, queue, recorder, lock, executor);
    }

    private GeneralMessageTunnelPusher assembleGeneralMessagePusher(Receiver receiver, AbstractTunnel tunnel) {
        return new GeneralMessageTunnelPusher(receiver, tunnel, queue, recorder, lock, executor);
    }
}
