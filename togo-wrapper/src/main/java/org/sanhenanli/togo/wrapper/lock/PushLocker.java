package org.sanhenanli.togo.wrapper.lock;

/**
 * datetime 2020/1/22 18:45
 *
 * @author zhouwenxiang
 */
public interface PushLocker {

    boolean tryLock(String lockKey);

    void unlock(String lockKey);
}
