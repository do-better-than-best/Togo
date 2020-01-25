package org.sanhenanli.togo.network.policy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sanhenanli.togo.network.trigger.PushTrigger;

/**
 * datetime 2020/1/16 10:02
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class RetryablePushPolicy extends PushPolicy {

    private RetryPolicy retryPolicy;

    public RetryablePushPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger, RetryPolicy retryPolicy) {
        super(tunnelPolicy, trigger);
        this.retryPolicy = retryPolicy;
    }
}
