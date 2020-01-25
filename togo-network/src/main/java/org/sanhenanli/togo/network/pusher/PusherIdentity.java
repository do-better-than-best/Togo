package org.sanhenanli.togo.network.pusher;

import lombok.AllArgsConstructor;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import lombok.Data;

/**
 * datetime 2020/1/18 21:53
 * 推送器标识
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Data
public class PusherIdentity {

    /**
     * 接收者
     */
    protected Receiver receiver;
    /**
     * 使用的通道
     */
    protected AbstractTunnel tunnel;

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }
        PusherIdentity o = (PusherIdentity) obj;
        return receiver.equals(o.getReceiver()) && tunnel.equals(o.getTunnel());
    }

    @Override
    public final int hashCode() {
        return this.receiver.hashCode() + this.tunnel.hashCode();
    }
}
