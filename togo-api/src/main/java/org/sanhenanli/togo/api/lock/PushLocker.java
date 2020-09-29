package org.sanhenanli.togo.api.lock;

/**
 * datetime 2020/1/22 18:45
 * 推送锁
 *
 * @author zhouwenxiang
 */
public interface PushLocker {

    /**
     * 尝试获取锁. 不阻塞
     * @param lockKey 锁名
     * @return true获取成功, false获取失败
     */
    boolean tryLock(String lockKey);

    /**
     * 释放锁
     * @param lockKey 锁名
     */
    void unlock(String lockKey);
}
