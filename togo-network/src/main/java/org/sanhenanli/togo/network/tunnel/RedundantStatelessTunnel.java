package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.valve.AbstractValve;
import org.sanhenanli.togo.network.valve.ValveTip;

import java.util.ArrayList;
import java.util.List;

/**
 * datetime 2020/1/15 15:52
 * 冗余无状态通道: 多个独立通道的组合, 在消息推送时逐一尝试, 一个成功即可
 *
 * @author zhouwenxiang
 */
public class RedundantStatelessTunnel extends AbstractTunnel {

    public RedundantStatelessTunnel(String name, AbstractPusher pusher) {
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
        return TunnelTip.error(name, tip.toString());
    }

    /**
     * 独立通道列表
     */
    private List<AbstractSingleTunnel> tunnels = new ArrayList<>(2);

    /**
     * 添加通道
     * @param abstractSingleTunnel 通道
     * @return this
     */
    public RedundantStatelessTunnel combine(AbstractSingleTunnel abstractSingleTunnel) {
        this.tunnels.add(abstractSingleTunnel);
        return this;
    }

    /**
     * 获取通道列表
     * @return 通道列表
     */
    public List<AbstractSingleTunnel> getTunnels() {
        return tunnels;
    }
}
