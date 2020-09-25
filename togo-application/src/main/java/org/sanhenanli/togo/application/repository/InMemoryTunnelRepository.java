package org.sanhenanli.togo.application.repository;

import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import org.sanhenanli.togo.wrapper.repository.TunnelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * datetime 2020/1/26 13:39
 * 默认单机内存通道管理
 *
 * @author zhouwenxiang
 */
public class InMemoryTunnelRepository implements TunnelRepository {

    private final Map<String, AbstractTunnel> tunnelMap = new ConcurrentHashMap<>(8);
    private final Map<String, List<String>> tagNameMap = new ConcurrentHashMap<>(4);

    @Override
    public AbstractTunnel getByName(String name) {
        return tunnelMap.get(name);
    }

    @Override
    public List<String> substanceNames(String tag) {
        return tagNameMap.get(tag);
    }

    @Override
    public void register(AbstractTunnel substance, String tag) {
        tunnelMap.put(substance.getName(), substance);
        if (tag != null) {
            if (tagNameMap.containsKey(tag)) {
                tagNameMap.put(tag, new ArrayList<>(2));
            }
            tagNameMap.get(tag).add(substance.getName());
        }
    }

    @Override
    public boolean nameInTag(String tag, String name) {
        return tagNameMap.get(tag) != null && tagNameMap.get(tag).contains(name);
    }
}
