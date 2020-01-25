package org.sanhenanli.togo.network.lock;

import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/22 15:21
 * 锁
 *
 * @author zhouwenxiang
 */
public interface PushLock {

    /**
     * 尝试获取该receiver*tunnel的锁. 不阻塞
     * @param receiver 接收者
     * @param tunnel 使用的通道
     * @return true获取锁成功, false获取锁失败
     */
    boolean tryLock(Receiver receiver, AbstractTunnel tunnel);

    /**
     * 释放该receiver*tunnel的锁
     * @param receiver 接收者
     * @param tunnel 使用的通道
     */
    void unlock(Receiver receiver, AbstractTunnel tunnel);
}
