package org.sanhenanli.togo.application.lock;

import org.sanhenanli.togo.wrapper.lock.PushLocker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * datetime 2020/1/18 20:32
 *
 * @author zhouwenxiang
 */
@Component
@ConditionalOnMissingBean(PushLocker.class)
public class InMemoryPushLock implements PushLocker {

    private Map<String, String> lockMap;

    @PostConstruct
    public void init() {
        lockMap = new ConcurrentHashMap<>(32);
    }

    @Override
    public boolean tryLock(String lockKey) {
        return lockMap.putIfAbsent(lockKey, "") == null;

    }

    @Override
    public void unlock(String lockKey) {
        lockMap.remove(lockKey);
    }

}
