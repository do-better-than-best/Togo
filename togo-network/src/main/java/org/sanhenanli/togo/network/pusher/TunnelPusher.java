package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.message.Message;

/**
 * datetime 2020/1/22 14:02
 * 单人单通道消息推送器
 *
 * @author zhouwenxiang
 */
public interface TunnelPusher {

    /**
     * 启动该推送器
     */
    void start();

    /**
     * 添加消息到该通道的待推队列
     * @param message 消息
     * @param head 是否优先推送, true是
     */
    void add(Message message, boolean head);

    /**
     * 持续消费消息队列并推送
     */
    void pushContinuously();

    /**
     * 推送单条消息
     * @param message 消息
     * @return 是否可继续下一条消息推送, true是
     */
    boolean pushContinuously(Message message);

    /**
     * 推送并处理结果
     * @param message 消息
     * @return 是否可继续下一条消息推送, true是
     */
    boolean postPush(Message message);

    /**
     * 客户端上报收到消息的回执
     * @param id 消息唯一id
     */
    void reportReceipt(String id);

}
