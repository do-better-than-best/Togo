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

    void add(String receiver, Message message, String tunnel, boolean head);

    void onConnect(String tunnel, String receiver);

    void reportReceipt(String messageId);

}
