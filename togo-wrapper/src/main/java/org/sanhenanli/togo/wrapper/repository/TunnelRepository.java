package org.sanhenanli.togo.wrapper.repository;

import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/24 16:41
 *
 * @author zhouwenxiang
 */
public interface TunnelRepository extends GroupRepository {

    AbstractTunnel getByName(String name);
}
