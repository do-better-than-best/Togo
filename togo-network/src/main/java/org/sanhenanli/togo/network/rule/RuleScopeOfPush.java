package org.sanhenanli.togo.network.rule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * datetime 2020/1/22 9:54
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RuleScopeOfPush implements RuleScope {

    public static final RuleScopeOfPush DEDAULT = successPush();

    protected boolean success;
    protected boolean attempt;
    protected boolean error;

    public static RuleScopeOfPush successPush() {
        return new RuleScopeOfPush(true, false, false);
    }
}
