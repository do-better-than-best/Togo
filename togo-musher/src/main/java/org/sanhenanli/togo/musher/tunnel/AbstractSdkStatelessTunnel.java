package org.sanhenanli.togo.musher.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;

/**
 * datetime 2020/1/25 18:42
 *
 * @author zhouwenxiang
 */
public abstract class AbstractSdkStatelessTunnel extends AbstractHttpStatelessTunnel {
    public AbstractSdkStatelessTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }
}
