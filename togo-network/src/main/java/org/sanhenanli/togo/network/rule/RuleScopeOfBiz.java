package org.sanhenanli.togo.network.rule;

import org.sanhenanli.togo.network.business.Business;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * datetime 2020/1/22 9:44
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RuleScopeOfBiz implements RuleScope {

    public static final RuleScopeOfBiz DEFAULT = RuleScopeOfBiz.allBiz();

    protected boolean allBiz;
    protected boolean eachBiz;
    protected boolean specificBiz;
    protected List<Business> specificBizs;

    public static RuleScopeOfBiz allBiz() {
        return new RuleScopeOfBiz(true, false, false, null);
    }
}
