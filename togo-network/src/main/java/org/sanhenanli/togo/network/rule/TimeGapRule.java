package org.sanhenanli.togo.network.rule;

import lombok.Getter;

/**
 * datetime 2020/1/22 14:27
 *
 * @author zhouwenxiang
 */
@Getter
public class TimeGapRule extends AbstractRule implements RuleWithDuration {

    protected long mills;
}
