package org.sanhenanli.togo.network.policy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/16 9:30
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PushTunnelPolicy {

    /**
     * push instantly
     */
    public static final PushTunnelPolicy INSTANTLY = new PushTunnelPolicy(false, false, false, 0);
    /**
     * push when connected
     */
    public static final PushTunnelPolicy STATEFUL = new PushTunnelPolicy(true, false, false, 0);

    /**
     * push and check receipt
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy duplex(long timeoutMills) {
        return new PushTunnelPolicy(false, true, false, timeoutMills);
    }

    /**
     * push when connected and check receipt
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy statefulDuplex(long timeoutMills) {
        return new PushTunnelPolicy(true, true, false, timeoutMills);
    }

    /**
     * push in order
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy ordered(long timeoutMills) {
        return new PushTunnelPolicy(false, true, true, timeoutMills);
    }

    /**
     * push in order when connected
     * @param timeoutMills receipt timeout mills
     * @return policy
     */
    public static PushTunnelPolicy statefulOrdered(long timeoutMills) {
        return new PushTunnelPolicy(true, true, true, timeoutMills);
    }

    private boolean stateful;
    private boolean duplex;
    private boolean ordered;
    private long timeoutMills;

    public void unableStateful() {
        this.stateful = false;
    }

}
