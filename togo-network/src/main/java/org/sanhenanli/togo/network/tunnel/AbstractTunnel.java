package org.sanhenanli.togo.network.tunnel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sanhenanli.togo.network.factory.Name;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;

/**
 * datetime 2020/1/18 22:38
 * 通道
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public abstract class AbstractTunnel extends Name implements Tunnel {

    /**
     * 注册到推送器
     */
    protected AbstractPusher pusher;

    public AbstractTunnel(String name, AbstractPusher pusher) {
        super(name);
        this.pusher = pusher;
    }

    /**
     * 执行推送: 有状态推送需检查连接状态
     * @param receiver 接收者
     * @param msg 消息
     * @return 推送结果
     */
    public abstract TunnelTip push(Receiver receiver, Message msg);
}
