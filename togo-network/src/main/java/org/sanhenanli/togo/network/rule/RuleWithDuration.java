package org.sanhenanli.togo.network.rule;

/**
 * datetime 2020/1/22 9:28
 * 带时长属性的规则
 *
 * @author zhouwenxiang
 */
public interface RuleWithDuration {

    /**
     * 获取时长 s
     * 小于1s的规则会丢失精度, 所以直接要求规则粒度大于等于秒
     * @return s
     */
    long getSeconds();
}
