package org.sanhenanli.togo.config.tunnel;

import lombok.extern.slf4j.Slf4j;
import org.sanhenanli.togo.network.pusher.AbstractPusher;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractStatelessTunnel;
import org.sanhenanli.togo.network.tunnel.TunnelTip;

/**
 * datetime 2020/9/28 9:15
 *
 * @author zhouwenxiang
 */
@Slf4j
public class LogTunnel extends AbstractStatelessTunnel {

    public LogTunnel(String name, AbstractPusher pusher) {
        super(name, pusher);
    }

    @Override
    public TunnelTip doPush(Receiver receiver, String msg) {
        String receiverName = receiver.getName();
        log.info("send to {}: {}", receiverName, msg);
        return TunnelTip.ok(name);
    }
}
