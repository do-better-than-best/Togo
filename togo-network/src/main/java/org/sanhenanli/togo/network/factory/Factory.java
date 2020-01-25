package org.sanhenanli.togo.network.factory;

import java.util.List;

/**
 * datetime 2020/1/22 11:10
 * 组管理器
 *
 * @author zhouwenxiang
 */
public interface Factory<T extends Group> {

    /**
     * 列出所有下级实体
     * @param superior 虚拟对象
     * @return 实体列表
     */
    List<T> inferiorSubstances(T superior);

    /**
     * 注册实体或虚拟, 注册上下级管理
     * @param inferior 下级
     * @param superior 上级
     */
    void register(T inferior, T superior);

    /**
     * 判断是否有上下级关系
     * @param superior 上级
     * @param inferior 下级
     * @return true有上下级关系, false没有
     */
    boolean hasHierarchy(T superior, T inferior);

    /**
     * 根据name获取实体
     * @param name name, 对象的唯一标识
     * @return 实体
     */
    T getSubstanceByName(String name);
}
