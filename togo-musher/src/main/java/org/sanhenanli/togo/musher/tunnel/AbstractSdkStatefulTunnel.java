package org.sanhenanli.togo.musher.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.tunnel.AbstractStatefulTunnel;

/**
 * datetime 2020/1/25 18:42
 * sdk有状态通道, 例个推sdk
 *
 * @author zhouwenxiang
 */
public abstract class AbstractSdkStatefulTunnel extends AbstractStatefulTunnel {
    public AbstractSdkStatefulTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }
}
