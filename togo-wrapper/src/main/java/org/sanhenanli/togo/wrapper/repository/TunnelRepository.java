package org.sanhenanli.togo.wrapper.repository;

import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/24 16:41
 * 通道管理
 *
 * @author zhouwenxiang
 */
public interface TunnelRepository extends GroupRepository {

    /**
     * 根据name获取通道实体
     * @param name name
     * @return 通道
     */
    AbstractTunnel getByName(String name);
}
