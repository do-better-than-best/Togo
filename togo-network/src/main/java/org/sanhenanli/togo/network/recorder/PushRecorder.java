package org.sanhenanli.togo.network.recorder;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.tunnel.TunnelTip;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/18 20:41
 *
 * @author zhouwenxiang
 */
public interface PushRecorder {

    LocalDateTime lastSuccessTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnels);
    LocalDateTime lastAttemptTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnels);
    LocalDateTime lastErrorTime(long number, Receiver receivers, Business biz, AbstractTunnel tunnels);

    void recordError(Message message, TunnelTip tip);

    void recordSuccess(Message message);

    void recordRetry(Message message, TunnelTip tip);
}
