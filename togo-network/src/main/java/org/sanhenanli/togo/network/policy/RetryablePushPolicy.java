package org.sanhenanli.togo.network.policy;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * datetime 2020/1/16 10:02
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RetryablePushPolicy extends PushPolicy {

    private RetryPolicy retryPolicy;
}
