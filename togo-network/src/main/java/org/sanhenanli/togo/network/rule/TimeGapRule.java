package org.sanhenanli.togo.network.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/22 14:27
 * 时间间隔推送规则, 限制在一定时长后才能推送
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class TimeGapRule extends AbstractRule implements RuleWithDuration {

    protected long mills;
}
