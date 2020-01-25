package org.sanhenanli.togo.network.policy;

import org.sanhenanli.togo.network.trigger.PushTrigger;
import lombok.Data;

/**
 * datetime 2020/1/15 17:47
 *
 * @author zhouwenxiang
 */
@Data
public class PushPolicy {

    protected PushTunnelPolicy tunnelPolicy;
    protected PushTrigger trigger;

    public PushPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger) {
        this.tunnelPolicy = tunnelPolicy;
        this.trigger = trigger;
    }
}
