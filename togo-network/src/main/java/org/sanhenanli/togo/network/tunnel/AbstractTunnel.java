package org.sanhenanli.togo.network.tunnel;

import lombok.Getter;
import org.sanhenanli.togo.network.factory.Group;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;

/**
 * datetime 2020/1/18 22:38
 *
 * @author zhouwenxiang
 */
@Getter
public abstract class AbstractTunnel extends Group implements Tunnel {

    protected String name;
    protected AbstractPusher pusher;

    public abstract TunnelTip push(Receiver receiver, Message msg);

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }
        AbstractTunnel o = (AbstractTunnel) obj;
        return this.name != null && this.name.equals(o.name);
    }

    @Override
    public final int hashCode() {
        return this.name.hashCode();
    }
}
