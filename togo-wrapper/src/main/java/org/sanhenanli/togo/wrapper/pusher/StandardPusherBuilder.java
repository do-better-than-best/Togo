package org.sanhenanli.togo.wrapper.pusher;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.business.BusinessFactory;
import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.lock.PushLock;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.message.MessageQueue;
import org.sanhenanli.togo.network.pusher.PusherIdentity;
import org.sanhenanli.togo.network.pusher.StandardPusher;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.receiver.ReceiverFactory;
import org.sanhenanli.togo.network.recorder.PushRecorder;
import org.sanhenanli.togo.network.trigger.PushTrigger;
import org.sanhenanli.togo.network.trigger.ScheduleTrigger;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import org.sanhenanli.togo.network.tunnel.TunnelFactory;
import org.sanhenanli.togo.network.tunnel.TunnelTip;
import org.sanhenanli.togo.wrapper.lock.PushLocker;
import org.sanhenanli.togo.wrapper.model.MessageDetail;
import org.sanhenanli.togo.wrapper.model.MessagePush;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;
import org.sanhenanli.togo.wrapper.repository.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * datetime 2020/1/24 15:40
 * 全局推送器builder
 *
 * @author zhouwenxiang
 */
public class StandardPusherBuilder {

    /**
     * 消息管理
     */
    protected MessageRepository messageRepository;
    /**
     * 消息推送管理
     */
    protected MessagePushRepository messagePushRepository;
    /**
     * 锁
     */
    protected PushLocker pushLocker;
    /**
     * 任务执行器
     */
    protected Executor executor;
    /**
     * 接收者管理
     */
    protected ReceiverRepository receiverRepository;
    /**
     * 业务管理
     */
    protected BusinessRepository businessRepository;
    /**
     * 通道管理
     */
    protected TunnelRepository tunnelRepository;

    public StandardPusherBuilder(MessageRepository messageRepository, MessagePushRepository messagePushRepository, PushLocker pushLocker, Executor executor, ReceiverRepository receiverRepository, BusinessRepository businessRepository, TunnelRepository tunnelRepository) {
        this.messageRepository = messageRepository;
        this.messagePushRepository = messagePushRepository;
        this.pushLocker = pushLocker;
        this.executor = executor;
        this.receiverRepository = receiverRepository;
        this.businessRepository = businessRepository;
        this.tunnelRepository = tunnelRepository;
    }

    public StandardPusher build() {
        return new StandardPusher(buildMessageQueue(), buildPushRecorder(), executor, buildPushLock(), buildReceiverFactory(), buildTunnelFactory(), buildBusinessFactory());
    }

    private MessageQueue buildMessageQueue() {
        return new MessageQueue() {
            @Override
            public void add(Receiver receiver, Message message, AbstractTunnel tunnel, boolean head) {
                MessageDetail messageDetail = buildMessageDetail(message, receiver, tunnel, head);
                if (messageDetail.getTryTime() != 0) {
                    messageRepository.updateMessageTryTimes(messageDetail.getId(), messageDetail.getTryTime());
                } else {
                    messageDetail = messageRepository.save(messageDetail);
                }
                messagePushRepository.saveMessagePush(buildMessagePush(messageDetail));
            }

            @Override
            public Message popOrderedMessage(Receiver receiver, AbstractTunnel tunnel) {
                MessagePush messagePush = messagePushRepository.popByReceiverAndTunnelAndOrderedAndStatusIsUnfinishedOrderByPushOrder(receiver.getName(), tunnel.getName(), true);
                MessageDetail messageDetail = messageRepository.findOne(messagePush.getMessageId());
                return buildMessage(messageDetail);
            }

            @Override
            public Message popStatefulMessage(Receiver receiver, AbstractTunnel tunnel) {
                MessagePush messagePush = messagePushRepository.popByReceiverAndTunnelAndOrderedAndStatefulAndStatusIsUnfinishedOrderByPushOrder(receiver.getName(), tunnel.getName(), false, true);
                MessageDetail messageDetail = messageRepository.findOne(messagePush.getMessageId());
                return buildMessage(messageDetail);
            }

            @Override
            public Message popGeneralMessage(Receiver receiver, AbstractTunnel tunnel) {
                MessagePush messagePush = messagePushRepository.popByReceiverAndTunnelAndOrderedAndStatefulAndStatusIsUnfinishedOrderByPushOrder(receiver.getName(), tunnel.getName(), false, false);
                MessageDetail messageDetail = messageRepository.findOne(messagePush.getMessageId());
                return buildMessage(messageDetail);
            }

            @Override
            public Set<PusherIdentity> pushersToTrigger() {
                return messagePushRepository.findDistinctReceiverAndTunnelByStatusIsUnfinished();
            }

            @Override
            public void reportReceipt(String messageId) {
                messageRepository.saveMessageReceipt(messageId);
            }

            @Override
            public boolean consumeReceipt(String messageId) {
                return messageRepository.hasReceipt(messageId);
            }
        };
    }

    private PushRecorder buildPushRecorder() {
        return new PushRecorder() {
            @Override
            public LocalDateTime lastSuccessTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel) {
                Set<String> receivers = new HashSet<>(receiverRepository.listByGroup(receiver.getName()));
                Set<String> bizs = new HashSet<>(businessRepository.listByGroup(biz.getName()));
                Set<String> tunnels = new HashSet<>(tunnelRepository.listByGroup(tunnel.getName()));
                return messagePushRepository.findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(number, receivers, bizs, tunnels, PushStatusEnum.succeed());
            }

            @Override
            public LocalDateTime lastAttemptTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel) {
                Set<String> receivers = new HashSet<>(receiverRepository.listByGroup(receiver.getName()));
                Set<String> bizs = new HashSet<>(businessRepository.listByGroup(biz.getName()));
                Set<String> tunnels = new HashSet<>(tunnelRepository.listByGroup(tunnel.getName()));
                return messagePushRepository.findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(number, receivers, bizs, tunnels, PushStatusEnum.finished());
            }

            @Override
            public LocalDateTime lastErrorTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel) {
                Set<String> receivers = new HashSet<>(receiverRepository.listByGroup(receiver.getName()));
                Set<String> bizs = new HashSet<>(businessRepository.listByGroup(biz.getName()));
                Set<String> tunnels = new HashSet<>(tunnelRepository.listByGroup(tunnel.getName()));
                return messagePushRepository.findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(number, receivers, bizs, tunnels, PushStatusEnum.failed());
            }

            @Override
            public void recordError(Message message, TunnelTip tip) {
                messagePushRepository.updateMessagePushStatus(message.getId(), PushStatusEnum.FAILED, tip.getCause(), tip.getTip());
                messageRepository.updateMessageStatus(message.getId(), PushStatusEnum.FAILED);
            }

            @Override
            public void recordSuccess(Message message) {
                messagePushRepository.updateMessagePushStatus(message.getId(), PushStatusEnum.SUCCESS, null, null);
                messageRepository.updateMessageStatus(message.getId(), PushStatusEnum.SUCCESS);
            }

            @Override
            public void recordRetry(Message message, TunnelTip tip) {
                messagePushRepository.updateMessagePushStatus(message.getId(), PushStatusEnum.FAILED, tip.getCause(), tip.getTip());
            }
        };
    }

    private PushLock buildPushLock() {
        return new PushLock() {
            @Override
            public boolean tryLock(Receiver receiver, AbstractTunnel tunnel) {
                return pushLocker.tryLock(lockKey(receiver, tunnel));
            }

            @Override
            public void unlock(Receiver receiver, AbstractTunnel tunnel) {
                pushLocker.unlock(lockKey(receiver, tunnel));
            }

            private String lockKey(Receiver receiver, AbstractTunnel tunnel) {
                return receiver.getName() + "@@@" + tunnel.getName();
            }
        };
    }

    private ReceiverFactory buildReceiverFactory() {
        return new ReceiverFactory() {
            @Override
            public List<Receiver> inferiorSubstances(Receiver superior) {
                return receiverRepository.listByGroup(superior.getName()).stream().map(Receiver::new).collect(Collectors.toList());
            }

            @Override
            public void register(Receiver inferior, Receiver superior) {
                receiverRepository.register(inferior.getName(), superior.getName());
            }

            @Override
            public boolean hasHierarchy(Receiver superior, Receiver inferior) {
                return receiverRepository.hasHierarchy(superior.getName(), inferior.getName());
            }

            @Override
            public Receiver getSubstanceByName(String name) {
                return new Receiver(name);
            }
        };
    }

    private BusinessFactory buildBusinessFactory() {
        return new BusinessFactory() {
            @Override
            public List<Business> inferiorSubstances(Business superior) {
                return businessRepository.listByGroup(superior.getName()).stream().map(Business::new).collect(Collectors.toList());
            }

            @Override
            public void register(Business inferior, Business superior) {
                businessRepository.register(inferior.getName(), superior.getName());
            }

            @Override
            public boolean hasHierarchy(Business superior, Business inferior) {
                return businessRepository.hasHierarchy(superior.getName(), inferior.getName());
            }

            @Override
            public Business getSubstanceByName(String name) {
                return new Business(name);
            }
        };
    }

    private TunnelFactory buildTunnelFactory() {
        return new TunnelFactory() {
            @Override
            public List<AbstractTunnel> inferiorSubstances(AbstractTunnel superior) {
                return tunnelRepository.listByGroup(superior.getName()).stream().map(this::getSubstanceByName).collect(Collectors.toList());
            }

            @Override
            public void register(AbstractTunnel inferior, AbstractTunnel superior) {
                tunnelRepository.register(inferior.getName(), superior.getName());
            }

            @Override
            public boolean hasHierarchy(AbstractTunnel superior, AbstractTunnel inferior) {
                return tunnelRepository.hasHierarchy(superior.getName(), inferior.getName());
            }

            @Override
            public AbstractTunnel getSubstanceByName(String name) {
                return tunnelRepository.getByName(name);
            }
        };
    }

    private MessageDetail buildMessageDetail(Message message, Receiver receiver, AbstractTunnel tunnel, boolean head) {
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setMessageId(message.getId());
        messageDetail.setBiz(message.getBiz().getName());
        messageDetail.setCreateTime(LocalDateTime.now());
        messageDetail.setData(message.getData());
        messageDetail.setHead(head);
        messageDetail.setTryTime(message.getTryTimes().get());
        messageDetail.setPolicy(message.getPolicy());
        messageDetail.setReceiver(receiver.getName());
        messageDetail.setStatus(PushStatusEnum.CREATED);
        messageDetail.setTunnel(tunnel.getName());
        return messageDetail;
    }

    private MessagePush buildMessagePush(MessageDetail messageDetail) {
        MessagePush messagePush = new MessagePush();
        messagePush.setBiz(messageDetail.getBiz());
        messagePush.setCreateTime(messageDetail.getCreateTime());
        messagePush.setMessageId(messageDetail.getId());
        messagePush.setDuplex(messageDetail.getPolicy().getTunnelPolicy().isDuplex());
        messagePush.setFollowSuggestion(messageDetail.getPolicy().getRetryPolicy().isFollowSuggestion());
        messagePush.setOrdered(messageDetail.getPolicy().getTunnelPolicy().isOrdered());
        if (messageDetail.isHead()) {
            Long pushOrder = messagePushRepository.findMinPushOrderByReceiverAndTunnelAndStatusIsUnfinished(messageDetail.getReceiver(), messageDetail.getTunnel());
            messagePush.setPushOrder(pushOrder == null ? null : pushOrder - 1);
        }
        messagePush.setReceiver(messageDetail.getReceiver());
        messagePush.setRetry(messageDetail.getPolicy().getRetryPolicy().getRetry());
        messagePush.setRetryDuplex(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().isDuplex());
        messagePush.setRetryOrdered(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().isOrdered());
        messagePush.setRetryStateful(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().isStateful());
        messagePush.setRetryTimeoutMills(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().getTimeoutMills());
        PushTrigger retryTrigger = messageDetail.getPolicy().getRetryPolicy().getTrigger();
        messagePush.setRetryTriggerTime(retryTrigger instanceof ScheduleTrigger ? ((ScheduleTrigger) retryTrigger).getSchedule() : null);
        messagePush.setStateful(messageDetail.getPolicy().getTunnelPolicy().isStateful());
        messagePush.setStatus(PushStatusEnum.CREATED);
        messagePush.setTimeoutMills(messageDetail.getPolicy().getTunnelPolicy().getTimeoutMills());
        PushTrigger pushTrigger = messageDetail.getPolicy().getTrigger();
        messagePush.setTriggerTime(pushTrigger instanceof ScheduleTrigger ? ((ScheduleTrigger) pushTrigger).getSchedule() : null);
        messagePush.setTryTimes(messageDetail.getTryTime());
        messagePush.setTunnel(messageDetail.getTunnel());
        return messagePush;
    }

    private Message buildMessage(MessageDetail messageDetail) {
        Message message = new Message();
        message.setId(messageDetail.getId());
        message.setBiz(new Business(messageDetail.getBiz()));
        message.setData(messageDetail.getData());
        message.setPolicy(messageDetail.getPolicy());
        return message;
    }
}
