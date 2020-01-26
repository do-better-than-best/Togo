package org.sanhenanli.togo.network.factory;

import java.util.List;

/**
 * datetime 2020/1/22 11:10
 * 标签管理器
 *
 * @author zhouwenxiang
 */
public interface TagFactory<T extends Name> {

    /**
     * 列出所有tag或name下的实体
     * @param tag tag/name
     * @return 实体列表
     */
    List<T> substances(String tag);

    /**
     * 注册实体, 并给实体添加tag
     * @param substance 实体
     * @param tag tag
     */
    void register(T substance, String tag);

    /**
     * 判断tag下是否有此实体名
     * @param tag tag/name
     * @param name name
     * @return true有, false没有
     */
    boolean nameInTag(String tag, String name);

    /**
     * 根据name获取实体
     * @param name name, 对象的唯一标识
     * @return 实体
     */
    T getSubstanceByName(String name);
}
