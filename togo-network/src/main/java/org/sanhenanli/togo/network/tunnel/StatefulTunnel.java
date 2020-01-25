package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.receiver.Receiver;

/**
 * datetime 2020/1/18 22:37
 * 有状态通道
 *
 * @author zhouwenxiang
 */
public interface StatefulTunnel extends Tunnel {

    /**
     * 判断接收者是否建立了通道连接
     * @param receiver 接收者
     * @return true已连接
     */
    boolean connected(Receiver receiver);

    /**
     * 仅在连接状态下推送此消息, 未连接则等待连接时尝试, 未连接不算做推送失败, 不记录重试次
     * @param receiver 接收者
     * @param msg 消息
     * @return 推送结果
     */
    TunnelTip pushWhenConnected(Receiver receiver, Message msg);
}
