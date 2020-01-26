package org.sanhenanli.togo.network.valve;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.rule.AbstractRule;
import org.sanhenanli.togo.network.rule.RuleScopeOfBiz;
import org.sanhenanli.togo.network.rule.RuleScopeOfReceiver;
import org.sanhenanli.togo.network.rule.RuleScopeOfTunnel;

/**
 * datetime 2020/1/21 13:44
 * 推送控制阀门
 *
 * @author zhouwenxiang
 */
public abstract class AbstractValve implements Valve {

    /**
     * 推送控制规则
     */
    protected AbstractRule rule;
    /**
     * 所属推送器
     */
    protected AbstractPusher pusher;

    public AbstractValve(AbstractRule rule) {
        setRule(rule);
    }

    /**
     * 设置规则
     * @param rule 规则
     */
    public void setRule(AbstractRule rule) {
        assert support(rule);
        this.rule = rule;
    }

    /**
     * 绑定到推送器
     * @param pusher 推送器
     */
    public void bindPusher(AbstractPusher pusher) {
        this.pusher = pusher;
    }

    /**
     * 根据规则和消息获取待控制的业务
     * @param msg 消息
     * @param ruleScopeOfBiz 规则描述
     * @return 业务
     */
    protected String bizToControl(Message msg, RuleScopeOfBiz ruleScopeOfBiz) {
        for (String specificBiz : ruleScopeOfBiz.getSpecificBizs()) {
            if (pusher.getBusinessFactory().nameInTag(specificBiz, msg.getBiz().getName())) {
                return specificBiz;
            }
        }
        return null;
    }

    /**
     * 根据规则和接收者获取待控制的接收者
     * @param receiver 接收者
     * @param ruleScopeOfReceiver 规则描述
     * @return 接收者
     */
    protected String receiverToControl(String receiver, RuleScopeOfReceiver ruleScopeOfReceiver) {
        for (String specificReceiver : ruleScopeOfReceiver.getSpecificReceivers()) {
            if (pusher.getReceiverFactory().nameInTag(specificReceiver, receiver)) {
                return specificReceiver;
            }
        }
        return null;
    }

    /**
     * 根据规则和通道获取待控制的接收者
     * @param tunnel 通道
     * @param ruleScopeOfTunnel 规则描述
     * @return 通道
     */
    protected String tunnelToControl(String tunnel, RuleScopeOfTunnel ruleScopeOfTunnel) {
        for (String specificTunnel : ruleScopeOfTunnel.getSpecificTunnels()) {
            if (pusher.getTunnelFactory().nameInTag(specificTunnel, tunnel)) {
                return specificTunnel;
            }
        }
        return null;
    }

}
