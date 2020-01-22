package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.business.BusinessFactory;
import org.sanhenanli.togo.network.message.MessageQueue;
import org.sanhenanli.togo.network.receiver.ReceiverFactory;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.tunnel.TunnelFactory;
import org.sanhenanli.togo.network.valve.AbstractValve;
import lombok.Getter;

import java.util.List;

/**
 * datetime 2020/1/22 11:34
 *
 * @author zhouwenxiang
 */
@Getter
public class AbstractPusher {

    protected PushRecorder recorder;
    protected List<AbstractValve> valves;
    protected MessageQueue queue;
    protected BusinessFactory businessFactory;
    protected ReceiverFactory receiverFactory;
    protected TunnelFactory tunnelFactory;
}
