package org.sanhenanli.togo.network.policy;

import org.sanhenanli.togo.network.trigger.PushTrigger;
import lombok.Data;

/**
 * datetime 2020/1/15 17:47
 * 推送策略
 *
 * @author zhouwenxiang
 */
@Data
public class PushPolicy {

    /**
     * 在通道中的推送策略
     */
    protected PushTunnelPolicy tunnelPolicy;

    /**
     * 推送触发机制
     */
    protected PushTrigger trigger;

    public PushPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger) {
        this.tunnelPolicy = tunnelPolicy;
        this.trigger = trigger;
    }
}
