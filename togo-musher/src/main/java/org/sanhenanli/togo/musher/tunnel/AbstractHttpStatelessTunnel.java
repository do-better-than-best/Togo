package org.sanhenanli.togo.musher.tunnel;

import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.tunnel.AbstractStatefulTunnel;
import org.springframework.http.HttpHeaders;

import java.util.Map;

/**
 * datetime 2020/1/25 16:04
 * 通过http调用的无状态通道, post/get
 *
 * @author zhouwenxiang
 */
public abstract class AbstractHttpStatelessTunnel extends AbstractStatefulTunnel {

    protected String url;
    protected HttpHeaders httpHeaders;

    public AbstractHttpStatelessTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }

    protected abstract HttpHeaders buildHeaders(String receiver, String msg);

    protected abstract Map<String, String> buildParams(String receiver, String msg);

}
