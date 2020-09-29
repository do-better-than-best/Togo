package org.sanhenanli.togo.network.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * datetime 2020/1/22 11:08
 * 时间窗口推送规则, 限制在一定时长内的推送数量
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@ToString
@Getter
public class TimeWindowRule extends AbstractRule implements RuleWithQuota, RuleWithDuration {

    protected long seconds;
    protected long quota;
}
