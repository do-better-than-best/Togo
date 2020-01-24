package org.sanhenanli.togo.wrapper.repository;

import org.sanhenanli.togo.network.pusher.PusherIdentity;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;
import org.sanhenanli.togo.wrapper.model.MessagePush;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * datetime 2020/1/24 14:23
 *
 * @author zhouwenxiang
 */
public interface MessagePushRepository {

    void saveMessagePush(MessagePush messagePush);

    void updateMessagePushStatus(String detailId, PushStatusEnum status, TunnelTipCauseEnum cause, String tip);

    LocalDateTime lastSuccessTime(long number, String receiver, String biz, String tunnel);

    LocalDateTime lastAttemptTime(long number, String receiver, String biz, String tunnel);

    LocalDateTime lastErrorTime(long number, String receivers, String biz, String tunnel);

    MessagePush findFirstByReceiverAndTunnelAndOrderedAndStatefulAndStatusInOrderByPushOrder(String receiver, String tunnel, boolean ordered, boolean stateful, Set<PushStatusEnum> status);

    MessagePush findFirstByReceiverAndTunnelAndOrderedAndStatusInOrderByPushOrder(String receiver, String tunnel, boolean ordered, Set<PushStatusEnum> status);

    Set<PusherIdentity> findDistinctReceiverAndTunnelByStatusIsUnfinished();

    Long findMinPushOrderByReceiverAndTunnelAndStatusIsUnfinished(String receiver, String tunnel);
}