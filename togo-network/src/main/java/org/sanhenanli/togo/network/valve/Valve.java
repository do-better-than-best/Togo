package org.sanhenanli.togo.network.valve;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.rule.AbstractRule;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/21 12:38
 * 推送控制阀门
 *
 * @author zhouwenxiang
 */
public interface Valve {

    /**
     * 控制此次推送
     * @param receiver 接收者
     * @param message 消息
     * @param tunnel 使用的通道
     * @return 控制结果
     */
    ValveTip control(Receiver receiver, Message message, AbstractTunnel tunnel);

    /**
     * 判断是否支持执行此规则
     * @param rule 规则
     * @return true支持
     */
    boolean support(AbstractRule rule);
}
