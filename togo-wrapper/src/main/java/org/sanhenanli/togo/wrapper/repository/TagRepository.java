package org.sanhenanli.togo.wrapper.repository;

import java.util.List;

/**
 * datetime 2020/1/24 16:40
 * 标签管理
 *
 * @author zhouwenxiang
 */
public interface TagRepository<T> {

    /**
     * 根据tag或name列出所有实体
     * @param tag tag/name
     * @return 实体列表
     */
    List<String> substanceNames(String tag);

    /**
     * 注册实体/下级和属组/上级
     * @param substance 实体/下级
     * @param tag 属组/上级
     */
    void register(T substance, String tag);

    /**
     * 判断tag里是否有此name实体
     * @param tag tag
     * @param name name
     * @return true有
     */
    boolean nameInTag(String tag, String name);
}
