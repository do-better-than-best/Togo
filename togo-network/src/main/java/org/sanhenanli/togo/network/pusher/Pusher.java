package org.sanhenanli.togo.network.pusher;

import org.sanhenanli.togo.network.message.Message;

/**
 * datetime 2020/1/18 21:17
 * 全局推送其
 *
 * @author zhouwenxiang
 */
public interface Pusher {

    /**
     * 项目启动时调用该方法
     */
    void onStart();

    /**
     * 添加待推消息
     * @param receiver 接收者
     * @param message 消息
     * @param tunnel 使用的通道
     * @param head 是否优先推送, true优先
     */
    void add(String receiver, Message message, String tunnel, boolean head);

    /**
     * 通道是否已连接
     * @param tunnel 通道
     * @param receiver 接收者
     * @return true已连接
     */
    boolean connected(String tunnel, String receiver);

    /**
     * 接收者建立通道连接时调用该方法
     * @param tunnel 通道名称
     * @param receiver 接收者
     */
    void onConnect(String tunnel, String receiver);

    /**
     * 客户端上报消息回执
     * @param messageId 业务方消息id
     * @param receiver 接收者
     * @param tunnel 使用的通道
     * @param biz 业务
     */
    void reportReceipt(String messageId, String receiver, String tunnel, String biz);

}
