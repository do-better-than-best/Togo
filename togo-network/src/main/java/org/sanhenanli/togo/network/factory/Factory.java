package org.sanhenanli.togo.network.factory;

import java.util.List;

/**
 * datetime 2020/1/22 11:10
 *
 * @author zhouwenxiang
 */
public interface Factory<T> {

    List<T> recursiveInferiors(T receiver);

    T superior(T receiver);

    void register(T receiver, T superior);

    boolean hasHierarchy(T superior, T inferior);

    T getByName(String name);
}
