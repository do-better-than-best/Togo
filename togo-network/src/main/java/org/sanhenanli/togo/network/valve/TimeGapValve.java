package org.sanhenanli.togo.network.valve;

import org.sanhenanli.togo.network.rule.AbstractRule;
import org.sanhenanli.togo.network.rule.TimeGapRule;

/**
 * datetime 2020/9/25 14:09
 * 时间间隔推送控制阀门: 控制两次推送间的时间间隔
 *
 * @author zhouwenxiang
 */
public class TimeGapValve extends TimeWindowValve {

    public TimeGapValve(AbstractRule rule) {
        super(rule);
    }

    @Override
    public boolean support(AbstractRule rule) {
        return rule instanceof TimeGapRule;
    }
}
