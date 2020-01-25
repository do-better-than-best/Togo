package org.sanhenanli.togo.network.policy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sanhenanli.togo.network.trigger.PushTrigger;

/**
 * datetime 2020/1/15 17:47
 * 推送策略
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class PushPolicy {

    /**
     * 在通道中的推送策略
     */
    protected PushTunnelPolicy tunnelPolicy;

    /**
     * 推送触发机制
     */
    protected PushTrigger trigger;

}
