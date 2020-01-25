package org.sanhenanli.togo.application.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractStatelessTunnel;
import org.sanhenanli.togo.network.tunnel.TunnelTip;

/**
 * datetime 2020/1/25 20:12
 *
 * @author zhouwenxiang
 */
public class TestStatelessTunnel extends AbstractStatelessTunnel {

    public TestStatelessTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }

    @Override
    public TunnelTip doPush(Receiver receiver, String msg) {
        return TunnelTip.ok();
    }
}
