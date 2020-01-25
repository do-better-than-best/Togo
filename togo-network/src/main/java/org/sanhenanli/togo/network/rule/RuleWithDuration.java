package org.sanhenanli.togo.network.rule;

/**
 * datetime 2020/1/22 9:28
 * 带时长属性的规则
 *
 * @author zhouwenxiang
 */
public interface RuleWithDuration {

    /**
     * 获取时长 ms
     * @return ms
     */
    long getMills();
}
