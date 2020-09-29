package org.sanhenanli.togo.application.repository;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.api.repository.BusinessRepository;

import java.util.Collections;
import java.util.List;

/**
 * datetime 2020/3/4 11:02
 * 简单业务组管理
 *
 * @author zhouwenxiang
 */
public class SimpleBusinessRepository implements BusinessRepository {

    @Override
    public List<String> substanceNames(String tag) {
        if (tag == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(tag);
        }
    }

    @Override
    public void register(Business substance, String tag) {

    }

    @Override
    public boolean nameInTag(String tag, String name) {
        return name.equals(tag);
    }
}
