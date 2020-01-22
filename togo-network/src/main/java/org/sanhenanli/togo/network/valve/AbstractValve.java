package org.sanhenanli.togo.network.valve;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.rule.AbstractRule;
import org.sanhenanli.togo.network.rule.RuleScopeOfBiz;
import org.sanhenanli.togo.network.rule.RuleScopeOfReceiver;
import org.sanhenanli.togo.network.rule.RuleScopeOfTunnel;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/21 13:44
 *
 * @author zhouwenxiang
 */
public abstract class AbstractValve implements Valve {

    protected AbstractRule rule;
    protected AbstractPusher pusher;

    public void setRule(AbstractRule rule) {
        if (support(rule)) {
            this.rule = rule;
        }
        throw new IllegalArgumentException("rule not supported in this valve");
    }

    public void bindPusher(AbstractPusher pusher) {
        this.pusher = pusher;
    }


    protected Business bizToControl(Message msg, RuleScopeOfBiz ruleScopeOfBiz) {
        for (Business specificBiz : ruleScopeOfBiz.getSpecificBizs()) {
            if (pusher.getBusinessFactory().hasHierarchy(specificBiz, msg.getBiz())) {
                return specificBiz;
            }
        }
        return null;
    }

    protected Receiver receiverToControl(Receiver receiver, RuleScopeOfReceiver ruleScopeOfReceiver) {
        for (Receiver specificReceiver : ruleScopeOfReceiver.getSpecificReceivers()) {
            if (pusher.getReceiverFactory().hasHierarchy(specificReceiver, receiver)) {
                return specificReceiver;
            }
        }
        return null;
    }

    protected AbstractTunnel tunnelToControl(AbstractTunnel tunnel, RuleScopeOfTunnel ruleScopeOfTunnel) {
        for (AbstractTunnel specificTunnel : ruleScopeOfTunnel.getSpecificTunnels()) {
            if (pusher.getTunnelFactory().hasHierarchy(specificTunnel, tunnel)) {
                return specificTunnel;
            }
        }
        return null;
    }

}
