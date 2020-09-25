package org.sanhenanli.togo.application.lock;

import org.sanhenanli.togo.wrapper.lock.PushLocker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * datetime 2020/1/18 20:32
 * 默认单机内存锁
 *
 * @author zhouwenxiang
 */
public class InMemoryPushLock implements PushLocker {

    private final Map<String, String> lockMap = new ConcurrentHashMap<>(32);

    @Override
    public boolean tryLock(String lockKey) {
        return lockMap.putIfAbsent(lockKey, "") == null;

    }

    @Override
    public void unlock(String lockKey) {
        lockMap.remove(lockKey);
    }

}
