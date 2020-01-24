package org.sanhenanli.togo.wrapper.repository;

import java.util.List;

/**
 * datetime 2020/1/24 16:40
 *
 * @author zhouwenxiang
 */
public interface GroupRepository {

    List<String> listByGroup(String group);

    void register(String substance, String group);

    boolean hasHierarchy(String group, String substance);
}
