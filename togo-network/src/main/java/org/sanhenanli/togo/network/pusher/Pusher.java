package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/18 21:17
 *
 * @author zhouwenxiang
 */
public interface Pusher {

    void onStart();

    void add(Receiver receiver, Message message, AbstractTunnel wrappedTunnel, boolean head);

    void onConnect(AbstractTunnel wrappedTunnel, Receiver receiver);

    void reportReceipt(String messageId);

}
