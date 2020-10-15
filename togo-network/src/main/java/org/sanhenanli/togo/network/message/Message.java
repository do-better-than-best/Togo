package org.sanhenanli.togo.network.message;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * datetime 2020/1/18 21:19
 * 消息
 *
 * @author zhouwenxiang
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -7338366165153600298L;
    /**
     * 消息的唯一id
     */
    protected String id;
    /**
     * 消息所属的业务
     */
    protected Business biz;
    /**
     * 要推送的消息数据
     */
    protected String data;
    /**
     * 可重试的推送策略
     */
    protected RetryablePushPolicy policy;
    /**
     * 推送尝试次数
     */
    protected final AtomicInteger tryTimes = new AtomicInteger(0);

    public Message(String id, Business biz, String data, RetryablePushPolicy policy) {
        this.id = id;
        this.biz = biz;
        this.data = data;
        this.policy = policy;
    }

    /**
     * 标记一次尝试
     * @return 尝试次数
     */
    public int markTried() {
        return this.tryTimes.addAndGet(1);
    }

    /**
     * 初始化已尝试次数
     * @param triedTimes 已尝试次数
     */
    public void initTried(int triedTimes) {
        this.tryTimes.set(triedTimes);
    }

    /**
     * 判断是否可以重试
     * @return true可以重试, false不可以
     */
    public boolean retryable() {
        return policy.getRetryPolicy().getRetry() >= tryTimes.get();
    }

    /**
     * 降级为无状态消息
     */
    public void unableStateful() {
        policy.getTunnelPolicy().unableStateful();
        policy.getRetryPolicy().getTunnelPolicy().unableStateful();
    }
}
