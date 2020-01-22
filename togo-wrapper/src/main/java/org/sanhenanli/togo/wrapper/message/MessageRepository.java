package org.sanhenanli.togo.wrapper.message;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.pusher.PusherIdentity;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import org.sanhenanli.togo.network.tunnel.TunnelTip;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * datetime 2020/1/22 18:58
 *
 * @author zhouwenxiang
 */
public interface MessageRepository {

    void save(MessageWrapper message, boolean head); // todo head怎么处理: 查此receiver此tunnel的第一条待推

    MessageWrapper findFirstByReceiverAndTunnelAndOrderedIsTrueAndStatueIsCreatedOrderByCreateTime(String receiver, String tunnel);

    void updateMessageStatus(String id, )




    void add(Receiver receiver, Message message, AbstractTunnel tunnel, boolean head);

    Message popOrderedMessage(Receiver receiver, AbstractTunnel tunnel);
    Message popStatefulMessage(Receiver receiver, AbstractTunnel tunnel);
    Message popGeneralMessage(Receiver receiver, AbstractTunnel tunnel);

    Set<PusherIdentity> pushersToTrigger();

    void onSuccess(Message message);

    void onError(Message message);

    void onAttempt(Message message);

    void reportReceipt(String messageId);

    boolean consumeReceipt(String messageId);



    LocalDateTime lastSuccessTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnels);
    LocalDateTime lastAttemptTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnels);
    LocalDateTime lastErrorTime(long number, Receiver receivers, Business biz, AbstractTunnel tunnels);

    void recordError(Message message, TunnelTip tip);

    void recordSuccess(Message message);

    void recordAttempt(Message message);
}
