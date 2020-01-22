package org.sanhenanli.togo.network.policy;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * datetime 2020/1/16 9:42
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RetryPolicy extends PushPolicy {

    private int retry;
    private boolean followSuggestiong;

}
