package org.sanhenanli.togo.network.rule;

import lombok.Getter;

import java.util.List;

/**
 * datetime 2020/1/22 10:24
 * 对接收者的规则描述
 *
 * @author zhouwenxiang
 */
@Getter
public class RuleScopeOfReceiver implements RuleScope {

    /**
     * 默认描述, 规则适用于每个接收者
     */
    public static final RuleScopeOfReceiver DEFAULT = eachReceiver();

    /**
     * 是否适用全局, 不区分接收者
     */
    protected boolean allReceiver;
    /**
     * 是否适用每个接收者
     */
    protected boolean eachReceiver;
    /**
     * 是否适用特定接收者
     */
    protected boolean specificReceiver;
    protected List<String> specificReceivers;

    public RuleScopeOfReceiver(boolean allReceiver, boolean eachReceiver, boolean specificReceiver, List<String> specificReceivers) {
        assert allReceiver || eachReceiver || specificReceiver;
        assert !specificReceiver || specificReceivers != null && !specificReceivers.isEmpty();
        this.allReceiver = allReceiver;
        this.eachReceiver = eachReceiver;
        this.specificReceiver = specificReceiver;
        this.specificReceivers = specificReceivers;
    }

    public static RuleScopeOfReceiver eachReceiver() {
        return new RuleScopeOfReceiver(false, true, false, null);
    }
}
