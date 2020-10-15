package org.sanhenanli.togo.network.policy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.sanhenanli.togo.network.trigger.PushTrigger;

import java.io.Serializable;

/**
 * datetime 2020/1/15 17:47
 * 推送策略
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
@Setter
public class PushPolicy implements Serializable {

    private static final long serialVersionUID = -2125192891179003384L;
    /**
     * 在通道中的推送策略
     */
    protected PushTunnelPolicy tunnelPolicy;

    /**
     * 推送触发机制
     */
    protected PushTrigger trigger;

}
