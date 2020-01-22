package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.model.Message;
import org.sanhenanli.togo.network.model.TunnelTip;
import org.sanhenanli.togo.network.receiver.Receiver;

/**
 * datetime 2020/1/18 22:37
 *
 * @author zhouwenxiang
 */
public interface StatefulTunnel extends Tunnel {

    boolean connected(Receiver receiver);

    TunnelTip pushWhenConnected(Receiver receiver, Message msg);
}
