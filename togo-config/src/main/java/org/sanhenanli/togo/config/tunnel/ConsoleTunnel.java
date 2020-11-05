package org.sanhenanli.togo.config.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractStatelessTunnel;
import org.sanhenanli.togo.network.tunnel.TunnelTip;

/**
 * datetime 2020/9/27 9:53
 * 控制台tunnel
 *
 * @author zhouwenxiang
 */
public class ConsoleTunnel extends AbstractStatelessTunnel {

    public ConsoleTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }

    @Override
    public TunnelTip doPush(Receiver receiver, String msg) {
        String receiverName = receiver.getName();
        msg = String.format("send to %s: %s", receiverName, msg);
        System.out.println(msg);
        return TunnelTip.ok(name);
    }
}
