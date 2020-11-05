package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.valve.ValveTip;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.valve.AbstractValve;

/**
 * datetime 2020/1/22 14:17
 * 无状态通道: 即不管理接收者连接状态
 *
 * @author zhouwenxiang
 */
public abstract class AbstractStatelessTunnel extends AbstractTunnel {

    public AbstractStatelessTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }

    @Override
    public TunnelTip push(Receiver receiver, Message msg) {
        for (AbstractValve valve : pusher.getValves()) {
            ValveTip valveTip = valve.control(receiver, msg, this);
            if (!valveTip.isOk()) {
                return TunnelTip.blocked(name, valveTip);
            }
        }
        return doPush(receiver, msg.getData());
    }
}
