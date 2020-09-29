package org.sanhenanli.togo.application.repository;

import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.api.repository.ReceiverRepository;

import java.util.Collections;
import java.util.List;

/**
 * datetime 2020/3/4 11:04
 * 简单目标组管理
 *
 * @author zhouwenxiang
 */
public class SimpleReceiverRepository implements ReceiverRepository {

    @Override
    public List<String> substanceNames(String tag) {
        if (tag == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(tag);
        }
    }

    @Override
    public void register(Receiver substance, String tag) {

    }

    @Override
    public boolean nameInTag(String tag, String name) {
        return name.equals(tag);
    }
}
