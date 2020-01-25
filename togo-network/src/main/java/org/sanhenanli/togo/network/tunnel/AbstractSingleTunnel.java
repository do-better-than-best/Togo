package org.sanhenanli.togo.network.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;

/**
 * datetime 2020/1/21 11:42
 * 独立通道
 *
 * @author zhouwenxiang
 */
public abstract class AbstractSingleTunnel extends AbstractTunnel {
    public AbstractSingleTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }
}
