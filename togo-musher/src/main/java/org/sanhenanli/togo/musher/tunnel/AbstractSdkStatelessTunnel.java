package org.sanhenanli.togo.musher.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.tunnel.AbstractStatelessTunnel;

/**
 * datetime 2020/1/25 18:42
 *
 * @author zhouwenxiang
 */
public abstract class AbstractSdkStatelessTunnel extends AbstractStatelessTunnel {
    public AbstractSdkStatelessTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }
}
