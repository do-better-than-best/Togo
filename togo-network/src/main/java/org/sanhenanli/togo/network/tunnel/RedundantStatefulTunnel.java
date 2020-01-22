package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.model.TunnelTip;
import org.sanhenanli.togo.network.receiver.Receiver;

import java.util.List;

/**
 * datetime 2020/1/16 10:58
 *
 * @author zhouwenxiang
 */
public class RedundantStatefulTunnel extends AbstractStatefulTunnel {

    @Override
    public boolean connected(Receiver receiver) {
        for (AbstractStatefulTunnel tunnel : tunnels) {
            if (tunnel.connected(receiver)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TunnelTip doPush(Receiver receiver, String msg) {
        StringBuilder tip = new StringBuilder();
        for (AbstractStatefulTunnel tunnel : tunnels) {
            TunnelTip tunnelTip = tunnel.doPush(receiver, msg);
            if (tunnelTip.isOk()) {
                return tunnelTip;
            } else {
                tip.append(tunnelTip.getTip());
            }
        }
        return TunnelTip.error(tip.toString());
    }

    private List<AbstractStatefulTunnel> tunnels;

    public RedundantStatefulTunnel combine(AbstractStatefulTunnel abstractSingleTunnel) {
        this.tunnels.add(abstractSingleTunnel);
        return this;
    }

    public List<AbstractStatefulTunnel> getTunnels() {
        return tunnels;
    }
}
