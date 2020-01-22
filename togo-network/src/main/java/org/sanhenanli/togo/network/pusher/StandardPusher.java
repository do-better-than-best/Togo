package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.business.BusinessFactory;
import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.lock.PushLock;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.message.MessageQueue;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.receiver.ReceiverFactory;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.tunnel.AbstractStatefulTunnel;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import org.sanhenanli.togo.network.tunnel.TunnelFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * datetime 2020/1/18 21:23
 *
 * @author zhouwenxiang
 */
public class StandardPusher implements Pusher {

    protected MessageQueue queue;
    protected PushRecorder recorder;
    protected Executor executor;
    protected PushLock lock;
    protected ReceiverFactory receiverFactory;
    protected TunnelFactory tunnelFactory;
    protected BusinessFactory businessFactory;

    public StandardPusher(MessageQueue queue, PushRecorder recorder, Executor executor, PushLock lock, ReceiverFactory receiverFactory, TunnelFactory tunnelFactory, BusinessFactory businessFactory) {
        this.queue = queue;
        this.recorder = recorder;
        this.executor = executor;
        this.lock = lock;
        this.receiverFactory = receiverFactory;
        this.tunnelFactory = tunnelFactory;
        this.businessFactory = businessFactory;
    }

    @Override
    public void onStart() {
        Set<PusherIdentity> pushers = queue.pushersToTrigger();
        assemblePusher(pushers).forEach(AbstractTunnelPusher::start);
    }

    @Override
    public void add(Receiver receiver, Message message, AbstractTunnel tunnel, boolean head) {
        List<Receiver> receivers = receiverFactory.recursiveInferiors(receiver);
        List<AbstractTunnel> tunnels = tunnelFactory.recursiveInferiors(tunnel);
        for (Receiver r : receivers) {
            for (AbstractTunnel t : tunnels) {
                if (message.getPolicy().getTunnelPolicy().isOrdered()) {
                    assembleOrderedMessagePusher(r, t).add(message, head);
                } else if (message.getPolicy().getTunnelPolicy().isStateful() && tunnel instanceof AbstractStatefulTunnel) {
                    assembleStatefulMessagePusher(r, t).add(message, head);
                } else {
                    assembleGeneralMessagePusher(r, t).add(message, head);
                }
            }
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
        return new OrderedMessageTunnelPusher(receiver, tunnel, queue, recorder, lock, executor, businessFactory);
    }

    private StatefulMessageTunnelPusher assembleStatefulMessagePusher(Receiver receiver, AbstractTunnel tunnel) {
        return new StatefulMessageTunnelPusher(receiver, tunnel, queue, recorder, lock, executor, businessFactory);
    }

    private GeneralMessageTunnelPusher assembleGeneralMessagePusher(Receiver receiver, AbstractTunnel tunnel) {
        return new GeneralMessageTunnelPusher(receiver, tunnel, queue, recorder, lock, executor, businessFactory);
    }
}
