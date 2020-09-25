package org.sanhenanli.togo.network.rule;

/**
 * datetime 2020/1/22 14:27
 * 时间间隔推送规则, 限制在一定时长后才能推送
 *
 * @author zhouwenxiang
 */
public class TimeGapRule extends TimeWindowRule {

    protected long quota = 1;

    public TimeGapRule(long mills) {
        super(mills, 1);
    }
}
