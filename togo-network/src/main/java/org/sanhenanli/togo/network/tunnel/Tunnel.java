package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.receiver.Receiver;

/**
 * datetime 2020/1/18 22:36
 * 通道
 *
 * @author zhouwenxiang
 */
public interface Tunnel {

    /**
     * 执行推送
     * @param receiver 接收者
     * @param msg 消息
     * @return 推送结果
     */
    TunnelTip doPush(Receiver receiver, String msg);
}
