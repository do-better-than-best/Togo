package org.sanhenanli.togo.network.rule;

import lombok.Getter;

/**
 * datetime 2020/1/21 14:08
 * 推送限制规则
 *
 * @author zhouwenxiang
 */
@Getter
public abstract class AbstractRule implements Rule{

    /**
     * 对接收者或接收者组的规则描述
     */
    protected RuleScopeOfReceiver receiverScope = RuleScopeOfReceiver.DEFAULT;

    /**
     * 对通道或通道组的规则描述
     */
    protected RuleScopeOfTunnel tunnelScope = RuleScopeOfTunnel.DEFAULT;

    /**
     * 对推送结果的规则描述
     */
    protected RuleScopeOfPush pushScope = RuleScopeOfPush.DEFAULT;

    /**
     * 对业务或业务组的规则描述
     */
    protected RuleScopeOfBiz bizScope = RuleScopeOfBiz.DEFAULT;

    public AbstractRule receiverScope(RuleScopeOfReceiver receiverScope) {
        this.receiverScope = receiverScope;
        return this;
    }

    public AbstractRule tunnelScope(RuleScopeOfTunnel tunnelScope) {
        this.tunnelScope = tunnelScope;
        return this;
    }

    public AbstractRule pushScope(RuleScopeOfPush pushScope) {
        this.pushScope = pushScope;
        return this;
    }

    public AbstractRule bizScope(RuleScopeOfBiz bizScope) {
        this.bizScope = bizScope;
        return this;
    }
}
