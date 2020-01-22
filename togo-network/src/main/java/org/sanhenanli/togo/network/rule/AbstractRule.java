package org.sanhenanli.togo.network.rule;

import lombok.Getter;

/**
 * datetime 2020/1/21 14:08
 *
 * @author zhouwenxiang
 */
@Getter
public abstract class AbstractRule implements Rule{

    protected RuleScopeOfReceiver receiverScope;
    protected RuleScopeOfTunnel tunnelScope;
    protected RuleScopeOfPush pushScope;
    protected RuleScopeOfBiz bizScope;
}
