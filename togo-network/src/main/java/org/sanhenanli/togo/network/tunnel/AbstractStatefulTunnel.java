package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.valve.ValveTip;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.valve.AbstractValve;

/**
 * datetime 2020/1/21 11:40
 * 有状态通道: 即可以判断接收者连接状态的通道
 *
 * @author zhouwenxiang
 */
public abstract class AbstractStatefulTunnel extends AbstractSingleTunnel implements StatefulTunnel {

    public AbstractStatefulTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }

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
