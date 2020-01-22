package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.message.Message;

/**
 * datetime 2020/1/22 14:02
 *
 * @author zhouwenxiang
 */
public interface TunnelPusher {

    void start();

    void add(Message message, boolean head);

    void pushContinuously();

    boolean pushContinuously(Message message);

    boolean postPush(Message message);

    void reportReceipt(String id);

}
