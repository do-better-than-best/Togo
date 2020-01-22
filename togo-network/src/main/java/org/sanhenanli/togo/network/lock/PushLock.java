package org.sanhenanli.togo.network.lock;

import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/22 15:21
 *
 * @author zhouwenxiang
 */
public interface PushLock {

    boolean tryLock(Receiver receiver, AbstractTunnel tunnel);

    void unlock(Receiver receiver, AbstractTunnel tunnel);
}
