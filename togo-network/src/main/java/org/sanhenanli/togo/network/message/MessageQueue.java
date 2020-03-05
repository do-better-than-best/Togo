package org.sanhenanli.togo.network.message;

import org.sanhenanli.togo.network.pusher.PusherIdentity;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.util.Set;

/**
 * datetime 2020/1/18 21:25
 * 消息队列
 *
 * @author zhouwenxiang
 */
public interface MessageQueue {

    /**
     * 添加消息到该receiver*tunnel的队列
     * @param receiver 接收者
     * @param message 消息
     * @param tunnel 使用的通道
     * @param head 是否加到队列头部, true加到头部, false加到尾部
     */
    void add(Receiver receiver, Message message, AbstractTunnel tunnel, boolean head);

    /**
     * 取出该receiver*tunnel的待推有序消息
     * @param receiver 接收者
     * @param tunnel 使用的通道
     * @return 消息
     */
    Message popOrderedMessage(Receiver receiver, AbstractTunnel tunnel);

    /**
     * 取出该receiver*tunnel的待推有状态消息
     * @param receiver 接收者
     * @param tunnel 使用的通道
     * @return 消息
     */
    Message popStatefulMessage(Receiver receiver, AbstractTunnel tunnel);

    /**
     * 取出该receiver*tunnel的待推普通消息
     * @param receiver 接收者
     * @param tunnel 使用的通道
     * @return 消息
     */
    Message popGeneralMessage(Receiver receiver, AbstractTunnel tunnel);

    /**
     * 从所有消息队列中统计有消息待推的receiver*tunnel
     * @return 推送器标识
     */
    Set<PusherIdentity> pushersToTrigger();

    /**
     * 客户端上报收到消息的回执
     * @param messageId 业务方消息id
     * @param biz 业务
     */
    void reportReceipt(String messageId, String biz);

    /**
     * 查看消息是否有回执
     * @param messageId 消息的唯一id
     * @return true有回执, false没有
     */
    boolean consumeReceipt(String messageId);

}
