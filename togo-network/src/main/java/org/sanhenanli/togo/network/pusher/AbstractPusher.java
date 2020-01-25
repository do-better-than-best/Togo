package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.business.BusinessFactory;
import org.sanhenanli.togo.network.message.MessageQueue;
import org.sanhenanli.togo.network.receiver.ReceiverFactory;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.tunnel.TunnelFactory;
import org.sanhenanli.togo.network.valve.AbstractValve;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * datetime 2020/1/22 11:34
 * 全局推送器
 *
 * @author zhouwenxiang
 */
@Getter
public abstract class AbstractPusher implements Pusher {

    /**
     * 全局推送记录器
     */
    protected PushRecorder recorder;
    /**
     * 推送控制阀门
     */
    protected List<AbstractValve> valves;
    /**
     * 全量消息队列
     */
    protected MessageQueue queue;
    /**
     * 业务管理器
     */
    protected BusinessFactory businessFactory;
    /**
     * 接收者管理器
     */
    protected ReceiverFactory receiverFactory;
    /**
     * 通道管理器
     */
    protected TunnelFactory tunnelFactory;

    /**
     * 添加推送控制阀门
     * @param valve 阀门
     * @return this
     */
    public AbstractPusher addValve(AbstractValve valve) {
        if (this.valves == null) {
            this.valves = new ArrayList<>();
        }
        valve.bindPusher(this);
        this.valves.add(valve);
        return this;
    }

}
