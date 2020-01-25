package org.sanhenanli.togo.wrapper.repository;

import java.util.List;

/**
 * datetime 2020/1/24 16:40
 * 组管理
 *
 * @author zhouwenxiang
 */
public interface GroupRepository {

    /**
     * 根据组名列出所有实体
     * @param group 组名
     * @return 实体列表
     */
    List<String> listByGroup(String group);

    /**
     * 注册实体/下级和属组/上级
     * @param inferior 实体/下级
     * @param superior 属组/上级
     */
    void register(String inferior, String superior);

    /**
     * 判断是否有上下级关系
     * @param superior 下级
     * @param inferior 上级
     * @return true有
     */
    boolean hasHierarchy(String superior, String inferior);
}
