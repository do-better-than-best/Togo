package org.sanhenanli.togo.network.rule;

import lombok.Getter;

import java.util.List;

/**
 * datetime 2020/1/22 9:44
 * 对业务的规则描述
 *
 * @author zhouwenxiang
 */
@Getter
public class RuleScopeOfBiz implements RuleScope {

    /**
     * 默认描述, 规则适用于全局
     */
    public static final RuleScopeOfBiz DEFAULT = new RuleScopeOfBiz(true, false, false, null);

    /**
     * 是否适用全局, 不区分业务
     */
    protected boolean allBiz;
    /**
     * 是否适用每个业务
     */
    protected boolean eachBiz;
    /**
     * 是否适用特定业务
     */
    protected boolean specificBiz;
    /**
     * 特定业务列表
     */
    protected List<String> specificBizs;

    public RuleScopeOfBiz(boolean allBiz, boolean eachBiz, boolean specificBiz, List<String> specificBizs) {
        assert allBiz || eachBiz || specificBiz;
        assert !specificBiz || specificBizs != null && !specificBizs.isEmpty();
        this.allBiz = allBiz;
        this.eachBiz = eachBiz;
        this.specificBiz = specificBiz;
        this.specificBizs = specificBizs;
    }

    public static RuleScopeOfBiz allBiz() {
        return new RuleScopeOfBiz(true, false, false, null);
    }
}
