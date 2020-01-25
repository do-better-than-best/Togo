package org.sanhenanli.togo.network.rule;

/**
 * datetime 2020/1/22 9:28
 * 带份额属性的规则
 *
 * @author zhouwenxiang
 */
public interface RuleWithQuota {

    /**
     * 获取份额
     * @return quota
     */
    long getQuota();
}
