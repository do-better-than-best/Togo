package org.sanhenanli.togo.network.policy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.sanhenanli.togo.network.trigger.InstantlyTrigger;
import org.sanhenanli.togo.network.trigger.PushTrigger;

import java.io.Serializable;

/**
 * datetime 2020/1/16 10:02
 * 可重试的推送策略
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class RetryablePushPolicy extends PushPolicy implements Serializable {

    private static final long serialVersionUID = -3744068926032850788L;
    /**
     * 重试策略
     */
    private RetryPolicy retryPolicy;

    public RetryablePushPolicy(PushTunnelPolicy tunnelPolicy, PushTrigger trigger, RetryPolicy retryPolicy) {
        super(tunnelPolicy, trigger);
        this.retryPolicy = retryPolicy;
    }

    /**
     * 默认策略, 无序, 无状态, 无回执, 实时发送, 不重试
     */
    public static RetryablePushPolicy defaultPolicy() {
        return new RetryablePushPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), RetryPolicy.defaultRetry(0));
    }
}
