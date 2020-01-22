package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.valve.ValveTip;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.valve.AbstractValve;

/**
 * datetime 2020/1/21 11:40
 *
 * @author zhouwenxiang
 */
public abstract class AbstractStatefulTunnel extends AbstractSingleTunnel implements StatefulTunnel {

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

    /**
     * check connection before pushing
     * @param msg data
     * @return TunnelResult
     */
    @Override
    public TunnelTip pushWhenConnected(Receiver receiver, Message msg) {
        for (AbstractValve valve : pusher.getValves()) {
            ValveTip valveTip = valve.control(receiver, msg, this);
            if (!valveTip.isOk()) {
                return TunnelTip.blocked(valveTip);
            }
        }
        if (connected(receiver)) {
            return doPush(receiver, msg.getData());
        }
        return TunnelTip.notConnected();
    }
}
