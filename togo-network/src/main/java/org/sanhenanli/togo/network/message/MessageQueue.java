package org.sanhenanli.togo.network.message;

import org.sanhenanli.togo.network.pusher.PusherIdentity;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.util.Set;

/**
 * datetime 2020/1/18 21:25
 *
 * @author zhouwenxiang
 */
public interface MessageQueue {

    void add(Receiver receiver, Message message, AbstractTunnel tunnel, boolean head);

    Message popOrderedMessage(Receiver receiver, AbstractTunnel tunnel);
    Message popStatefulMessage(Receiver receiver, AbstractTunnel tunnel);
    Message popGeneralMessage(Receiver receiver, AbstractTunnel tunnel);

    Set<PusherIdentity> pushersToTrigger();

    void onSuccess(Message message);

    void onError(Message message);

    void onAttempt(Message message);

    void reportReceipt(String messageId);

    boolean consumeReceipt(String messageId);

}
