package org.sanhenanli.togo.musher.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.tunnel.AbstractStatefulTunnel;

/**
 * datetime 2020/1/25 18:41
 * todo 通过http调用的有状态通道, 可以搭一个websocket当http通道测试
 *
 * @author zhouwenxiang
 */
public abstract class AbstractHttpStatefulTunnel extends AbstractStatefulTunnel {
    public AbstractHttpStatefulTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }
}
