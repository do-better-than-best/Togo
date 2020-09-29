package org.sanhenanli.togo.network.rule;

/**
 * datetime 2020/1/22 14:27
 * 时间间隔推送规则, 限制在一定时长后才能推送
 *
 * @author zhouwenxiang
 */
public class TimeGapRule extends TimeWindowRule {

    public TimeGapRule(long seconds) {
        super(seconds, 1);
    }
}
