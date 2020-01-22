package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.receiver.Receiver;

import java.util.List;

/**
 * datetime 2020/1/15 15:52
 *
 * @author zhouwenxiang
 */
public class RedundantStatelessTunnel extends AbstractStatelessTunnel {

    @Override
    public TunnelTip doPush(Receiver receiver, String msg) {
        StringBuilder tip = new StringBuilder();
        for (AbstractSingleTunnel tunnel : tunnels) {
            TunnelTip tunnelTip = tunnel.doPush(receiver, msg);
            if (tunnelTip.isOk()) {
                return tunnelTip;
            } else {
                tip.append(tunnelTip.getTip());
            }
        }
        return TunnelTip.error(tip.toString());
    }

    private List<AbstractSingleTunnel> tunnels;

    public RedundantStatelessTunnel combine(AbstractSingleTunnel abstractSingleTunnel) {
        this.tunnels.add(abstractSingleTunnel);
        return this;
    }

    public List<AbstractSingleTunnel> getTunnels() {
        return tunnels;
    }
}
