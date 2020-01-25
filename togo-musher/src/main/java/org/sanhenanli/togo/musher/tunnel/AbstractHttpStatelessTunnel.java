package org.sanhenanli.togo.musher.tunnel;

import org.sanhenanli.togo.network.tunnel.AbstractStatefulTunnel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * datetime 2020/1/25 16:04
 * todo 通过http调用的无状态通道, post或get
 *
 * @author zhouwenxiang
 */
public abstract class AbstractHttpStatelessTunnel extends AbstractStatefulTunnel {

    protected HttpMethod method;
    protected String url;
    protected HttpHeaders httpHeaders;

    protected abstract HttpHeaders buildHeaders(String receiver, String msg);

    protected abstract Map<String, String> buildParams(String receiver, String msg);

}
