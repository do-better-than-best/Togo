package org.sanhenanli.togo.network.policy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sanhenanli.togo.network.trigger.InstantlyTrigger;
import org.sanhenanli.togo.network.trigger.PushTrigger;

import java.io.Serializable;

/**
 * datetime 2020/1/16 9:42
 * 消息推送重试策略
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class RetryPolicy extends PushPolicy implements Serializable {

    private static final long serialVersionUID = 4518835314145141069L;
    /**
     * 总共可重试次数, 0不可重试
     */
    private final int retry;

    /**
     * 是否在建议时间重试, true是, false否则完全按照重试策略的触发器来触发重试
     */
    private final boolean followSuggestion;

    public RetryPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger, int retry, boolean followSuggestion) {
        super(tunnelPolicy, trigger);
        this.retry = retry;
        this.followSuggestion = followSuggestion;
    }

    /**
     * 默认重试(无状态推送, 即时触发, 不采纳建议时间)
     * @param retry 自定义重试次数
     * @return policy
     */
    public static RetryPolicy defaultRetry(int retry) {
        return new RetryPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), retry, false);
    }
}
