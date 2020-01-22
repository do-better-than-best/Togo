package org.sanhenanli.togo.network.model;

import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import lombok.Data;

/**
 * datetime 2020/1/18 21:53
 *
 * @author zhouwenxiang
 */
@Data
public class PusherIdentity {

    protected Receiver receiver;
    protected AbstractTunnel tunnel;

    public PusherIdentity(Receiver receiver, AbstractTunnel tunnel) {
        this.receiver = receiver;
        this.tunnel = tunnel;
    }

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
