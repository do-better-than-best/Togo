package org.sanhenanli.togo.network.message;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * datetime 2020/1/18 21:19
 *
 * @author zhouwenxiang
 */
@Data
public class Message {

    protected String id;
    protected Business biz;
    protected String data;
    protected RetryablePushPolicy policy;
    protected final AtomicInteger tryTimes = new AtomicInteger(0);

    public int markTried() {
        return this.tryTimes.addAndGet(1);
    }

    public boolean retryable() {
        return policy.getRetryPolicy().getRetry() >= tryTimes.get();
    }

    public void unableStateful() {
        policy.getTunnelPolicy().unableStateful();
        policy.getRetryPolicy().getTunnelPolicy().unableStateful();
    }
}
