package org.sanhenanli.togo.network.rule;

import lombok.Getter;

/**
 * datetime 2020/1/22 9:54
 * 对推送结果的规则描述
 *
 * @author zhouwenxiang
 */
@Getter
public class RuleScopeOfPush implements RuleScope {

    /**
     * 默认描述, 根据历史上的成功推送来控制推送
     */
    public static final RuleScopeOfPush DEFAULT = new RuleScopeOfPush(true, false, false);

    /**
     * 针对成功推送的规则
     */
    protected boolean success;
    /**
     * 针对推送的规则, 不论成功失败
     */
    protected boolean attempt;
    /**
     * 针对失败推送的规则
     */
    protected boolean error;

    public RuleScopeOfPush(boolean success, boolean attempt, boolean error) {
        if (attempt) {
            this.attempt = true;
        } else {
            this.success = success;
            this.attempt = attempt;
            this.error = error;
        }
    }
}
