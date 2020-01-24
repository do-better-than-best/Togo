package org.sanhenanli.togo.network.factory;

import java.util.List;

/**
 * datetime 2020/1/22 11:10
 *
 * @author zhouwenxiang
 */
public interface Factory<T extends Group> {

    List<T> inferiorSubstances(T superior);

    void register(T inferior, T superior);

    boolean hasHierarchy(T superior, T inferior);

    T getSubstanceByName(String name);
}
