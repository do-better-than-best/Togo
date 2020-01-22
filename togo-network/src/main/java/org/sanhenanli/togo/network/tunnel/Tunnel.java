package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.model.TunnelTip;
import org.sanhenanli.togo.network.receiver.Receiver;

/**
 * datetime 2020/1/18 22:36
 *
 * @author zhouwenxiang
 */
public interface Tunnel {

    /**
     * push msg
     *
     * @param msg msg
     */
    TunnelTip doPush(Receiver receiver, String msg);
}
