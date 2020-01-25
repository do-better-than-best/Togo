package org.sanhenanli.togo.network.policy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sanhenanli.togo.network.trigger.PushTrigger;

/**
 * datetime 2020/1/16 9:42
 * 消息推送重试策略
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class RetryPolicy extends PushPolicy {

    /**
     * 总共可重试次数, 0不可重试
     */
    private int retry;

    /**
     * 是否在建议时间重试, true是, false否则完全按照重试策略的触发器来触发重试
     */
    private boolean followSuggestion;

    public RetryPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger, int retry, boolean followSuggestion) {
        super(tunnelPolicy, trigger);
        this.retry = retry;
        this.followSuggestion = followSuggestion;
    }
}
