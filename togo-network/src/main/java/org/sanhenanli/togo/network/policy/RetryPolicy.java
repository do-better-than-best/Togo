package org.sanhenanli.togo.network.policy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sanhenanli.togo.network.trigger.PushTrigger;

/**
 * datetime 2020/1/16 9:42
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class RetryPolicy extends PushPolicy {

    private int retry;
    private boolean followSuggestion;

    public RetryPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger, int retry, boolean followSuggestion) {
        super(tunnelPolicy, trigger);
        this.retry = retry;
        this.followSuggestion = followSuggestion;
    }
}
