package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.valve.AbstractValve;
import org.sanhenanli.togo.network.valve.ValveTip;

import java.util.ArrayList;
import java.util.List;

/**
 * datetime 2020/1/16 10:58
 * 冗余有状态通道: 多个有状态通道的组合, 在消息推送时逐一尝试, 一个成功即可
 *
 * @author zhouwenxiang
 */
public class RedundantStatefulTunnel extends AbstractTunnel implements StatefulTunnel {

    public RedundantStatefulTunnel(String name, AbstractPusher pusher) {
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

    @Override
    public TunnelTip pushWhenConnected(Receiver receiver, Message msg) {
        for (AbstractValve valve : pusher.getValves()) {
            ValveTip valveTip = valve.control(receiver, msg, this);
            if (!valveTip.isOk()) {
                return TunnelTip.blocked(name, valveTip);
            }
        }
        if (connected(receiver)) {
            return doPush(receiver, msg.getData());
        }
        return TunnelTip.notConnected(name);
    }

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
        return TunnelTip.error(name, tip.toString());
    }

    /**
     * 有状态独立通道列表
     */
    private List<AbstractStatefulTunnel> tunnels = new ArrayList<>(2);

    /**
     * 添加通道
     * @param abstractSingleTunnel 通道
     * @return this
     */
    public RedundantStatefulTunnel combine(AbstractStatefulTunnel abstractSingleTunnel) {
        this.tunnels.add(abstractSingleTunnel);
        return this;
    }

    /**
     * 获取通道列表
     * @return 通道列表
     */
    public List<AbstractStatefulTunnel> getTunnels() {
        return tunnels;
    }
}
