package org.sanhenanli.togo.network.valve;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.rule.AbstractRule;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

/**
 * datetime 2020/1/21 12:38
 *
 * @author zhouwenxiang
 */
public interface Valve {

    ValveTip control(Receiver receiver, Message message, AbstractTunnel tunnel);

    boolean support(AbstractRule rule);
}
