package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.model.Message;
import org.sanhenanli.togo.network.model.TunnelTip;
import org.sanhenanli.togo.network.model.ValveTip;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.valve.AbstractValve;

/**
 * datetime 2020/1/22 14:17
 *
 * @author zhouwenxiang
 */
public abstract class AbstractStatelessTunnel extends AbstractTunnel {
    @Override
    public TunnelTip push(Receiver receiver, Message msg) {
        for (AbstractValve valve : pusher.getValves()) {
            ValveTip valveTip = valve.control(receiver, msg, this);
            if (!valveTip.isOk()) {
                return TunnelTip.blocked(valveTip);
            }
        }
        return doPush(receiver, msg.getData());
    }
}
