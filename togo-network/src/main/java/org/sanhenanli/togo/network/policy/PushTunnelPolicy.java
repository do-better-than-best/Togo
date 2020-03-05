package org.sanhenanli.togo.network.policy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/16 9:30
 * 消息在通道中的推送策略
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class PushTunnelPolicy {

    /**
     * 立即推送
     * push instantly
     */
    public static final PushTunnelPolicy INSTANTLY = new PushTunnelPolicy(false, false, false, 0);
    /**
     * 通道建立连接后推送
     * push when connected
     */
    public static final PushTunnelPolicy STATEFUL = new PushTunnelPolicy(true, false, false, 0);

    /**
     * 推送后需要检查消息回执
     * push and check receipt
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy duplex(long timeoutMills) {
        return new PushTunnelPolicy(false, true, false, timeoutMills);
    }

    /**
     * 通道连接后推送, 并且检查消息回执
     * push when connected and check receipt
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy statefulDuplex(long timeoutMills) {
        return new PushTunnelPolicy(true, true, false, timeoutMills);
    }

    /**
     * 有序地推送消息, 必须检查消息回执
     * push in order
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy ordered(long timeoutMills) {
        return new PushTunnelPolicy(false, true, true, timeoutMills);
    }

    /**
     * 在通道连接后有序地推送消息, 必须检查消息回执
     * push in order when connected
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy statefulOrdered(long timeoutMills) {
        return new PushTunnelPolicy(true, true, true, timeoutMills);
    }

    /**
     * 是否在连接状态推送, true是
     */
    private boolean stateful;

    /**
     * 是否需要检查消息回执, true是
     */
    private boolean duplex;

    /**
     * 是否有序地推送, true是
     */
    private boolean ordered;

    /**
     * 推送后等待回执地时间, ms
     */
    private long timeoutMills;

    /**
     * 降级为不需要连接后推送
     */
    public void unableStateful() {
        this.stateful = false;
    }

}
