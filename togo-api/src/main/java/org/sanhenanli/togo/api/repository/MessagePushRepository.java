package org.sanhenanli.togo.api.repository;

import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.sanhenanli.togo.network.pusher.PusherIdentity;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;
import org.sanhenanli.togo.api.model.MessagePush;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * datetime 2020/1/24 14:23
 * 消息推送管理
 *
 * @author zhouwenxiang
 */
public interface MessagePushRepository {

    /**
     * 保存一个推送
     * @param messagePush 消息推送
     * @param head 是否优先推送
     */
    void saveMessagePush(MessagePush messagePush, boolean head);

    /**
     * 更新推送的状态: 寻找该消息id下的最近一次未完成推送, 修改其状态和结束时间
     * @param messageId 消息唯一id
     * @param status 状态
     * @param cause 原因
     * @param tip 描述
     */
    void updateMessagePushStatus(String messageId, PushStatusEnum status, TunnelTipCauseEnum cause, String tip);

    /**
     * 根据receiver, tunnel, statusCollection查询更新status
     * @param receiver 接收者
     * @param tunnel 使用的通道
     * @param status 更新为状态
     * @param statusCollection 待更新状态
     */
    void updateStatusByReceiverAndTunnelAndStatusIn(String receiver, String tunnel, PushStatusEnum status, Collection<PushStatusEnum> statusCollection);

    /**
     * 查找最近第n个推送的时间
     * @param number n
     * @param receivers 接收者列表, 为空则查找全部
     * @param bizs 业务列表, 为空则查找全部
     * @param tunnels 通道列表, 为空则查找全部
     * @param status 状态列表
     * @return 推送完成时间
     */
    LocalDateTime findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(long number, Set<String> receivers, Set<String> bizs, Set<String> tunnels, Set<PushStatusEnum> status);

    /**
     * 查找第一个待推, 并修改状态为推送中, 记录开始时间; 如果是定时推送的消息, pop出可推的messagePush, triggerTime未到的不pop
     * @param receiver 接受者
     * @param tunnel 通道
     * @param ordered 是否有序推送
     * @param stateful 是否有状态推送
     * @return 消息推送
     */
    MessagePush popByReceiverAndTunnelAndOrderedAndStatefulAndStatusIsUnfinishedOrderByPushOrder(String receiver, String tunnel, boolean ordered, boolean stateful);

    /**
     * 查找第一个待推, 并修改状态为推送中, 记录开始时间; 如果是定时推送的消息, pop出可推的messagePush, triggerTime未到的不pop
     * @param receiver 接受者
     * @param tunnel 通道
     * @param ordered 是否有序推送
     * @return 消息推送
     */
    MessagePush popByReceiverAndTunnelAndOrderedAndStatusIsUnfinishedOrderByPushOrder(String receiver, String tunnel, boolean ordered);

    /**
     * 查找待启动的推送器, 即receiver*tunnel有为完成的消息推送
     * @return 推送器标识
     */
    Set<PusherIdentity> findDistinctReceiverAndTunnelByStatusIsUnfinished();
}
