package org.sanhenanli.togo.network.policy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.sanhenanli.togo.network.trigger.PushTrigger;

/**
 * datetime 2020/1/16 10:02
 * 可重试的推送策略
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class RetryablePushPolicy extends PushPolicy {

    /**
     * 重试策略
     */
    private RetryPolicy retryPolicy;

    public RetryablePushPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger, RetryPolicy retryPolicy) {
        super(tunnelPolicy, trigger);
        this.retryPolicy = retryPolicy;
    }
}
