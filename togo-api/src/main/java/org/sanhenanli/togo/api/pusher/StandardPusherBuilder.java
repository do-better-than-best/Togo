package org.sanhenanli.togo.api.pusher;

import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.sanhenanli.togo.api.repository.*;
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
import org.sanhenanli.togo.api.lock.PushLocker;
import org.sanhenanli.togo.api.model.MessageDetail;
import org.sanhenanli.togo.api.model.MessagePush;
import org.sanhenanli.togo.api.repository.*;

import java.time.LocalDateTime;
import java.util.*;
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

    protected MessageQueue buildMessageQueue() {
        return new MessageQueue() {
            @Override
            public void add(Receiver receiver, Message message, AbstractTunnel tunnel, boolean head) {
                MessageDetail messageDetail = buildMessageDetail(message, receiver, tunnel, head);
                if (messageDetail.getTryTimes() != 0) {
                    // 是重试消息, id系统转化(重试时插入的message的id是message的唯一id, message的messageId是提交方业务里的messageId)
                    messageDetail.setMessageId(null);
                    messageDetail.setId(message.getId());
                } else {
                    // 非重试消息, 保存此消息
                    messageDetail = messageRepository.save(messageDetail);
                }
                if (messageDetail != null) {
                    // 每进入一条新消息或重试一条消息, 初始化create状态的messagePush
                    messagePushRepository.saveMessagePush(buildMessagePush(messageDetail), messageDetail.isHead());
                }
            }

            @Override
            public void rollback(Receiver receiver, AbstractTunnel tunnel) {
                messagePushRepository.updateStatusByReceiverAndTunnelAndStatusIn(receiver.getName(), tunnel.getName(), PushStatusEnum.CREATED, Collections.singletonList(PushStatusEnum.PUSHING));
            }

            @Override
            public Message popOrderedMessage(Receiver receiver, AbstractTunnel tunnel) {
                MessagePush messagePush = messagePushRepository.popByReceiverAndTunnelAndOrderedAndStatusIsUnfinishedOrderByPushOrder(receiver.getName(), tunnel.getName(), true);
                if (messagePush == null) {
                    return null;
                }
                MessageDetail messageDetail = messageRepository.findOne(messagePush.getMessageId());
                return buildMessage(messageDetail);
            }

            @Override
            public Message popStatefulMessage(Receiver receiver, AbstractTunnel tunnel) {
                MessagePush messagePush = messagePushRepository.popByReceiverAndTunnelAndOrderedAndStatefulAndStatusIsUnfinishedOrderByPushOrder(receiver.getName(), tunnel.getName(), false, true);
                if (messagePush == null) {
                    return null;
                }
                MessageDetail messageDetail = messageRepository.findOne(messagePush.getMessageId());
                return buildMessage(messageDetail);
            }

            @Override
            public Message popGeneralMessage(Receiver receiver, AbstractTunnel tunnel) {
                MessagePush messagePush = messagePushRepository.popByReceiverAndTunnelAndOrderedAndStatefulAndStatusIsUnfinishedOrderByPushOrder(receiver.getName(), tunnel.getName(), false, false);
                if (messagePush == null) {
                    return null;
                }
                MessageDetail messageDetail = messageRepository.findOne(messagePush.getMessageId());
                return buildMessage(messageDetail);
            }

            @Override
            public Set<PusherIdentity> pushersToTrigger() {
                return messagePushRepository.findDistinctReceiverAndTunnelByStatusIsUnfinished();
            }

            @Override
            public void reportReceipt(String messageId, Receiver receiver, AbstractTunnel tunnel, Business biz) {
                messageRepository.saveMessageReceipt(messageId, receiver.getName(), tunnel.getName(), biz.getName());
            }

            @Override
            public boolean consumeReceipt(String messageId) {
                return messageRepository.hasReceipt(messageId);
            }
        };
    }

    protected PushRecorder buildPushRecorder() {
        return new PushRecorder() {
            @Override
            public LocalDateTime lastSuccessTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel) {
                Set<String> receivers = receiver == null ? null : new HashSet<>(receiverRepository.substanceNames(receiver.getName()));
                Set<String> bizs = biz == null ? null : new HashSet<>(businessRepository.substanceNames(biz.getName()));
                Set<String> tunnels = tunnel == null ? null : new HashSet<>(tunnelRepository.substanceNames(tunnel.getName()));
                return messagePushRepository.findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(number, receivers, bizs, tunnels, PushStatusEnum.succeed());
            }

            @Override
            public LocalDateTime lastAttemptTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel) {
                Set<String> receivers = receiver == null ? null : new HashSet<>(receiverRepository.substanceNames(receiver.getName()));
                Set<String> bizs = biz == null ? null : new HashSet<>(businessRepository.substanceNames(biz.getName()));
                Set<String> tunnels = tunnel == null ? null : new HashSet<>(tunnelRepository.substanceNames(tunnel.getName()));
                return messagePushRepository.findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(number, receivers, bizs, tunnels, PushStatusEnum.finished());
            }

            @Override
            public LocalDateTime lastErrorTime(long number, Receiver receiver, Business biz, AbstractTunnel tunnel) {
                Set<String> receivers = receiver == null ? null : new HashSet<>(receiverRepository.substanceNames(receiver.getName()));
                Set<String> bizs = biz == null ? null : new HashSet<>(businessRepository.substanceNames(biz.getName()));
                Set<String> tunnels = tunnel == null ? null : new HashSet<>(tunnelRepository.substanceNames(tunnel.getName()));
                return messagePushRepository.findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(number, receivers, bizs, tunnels, PushStatusEnum.failed());
            }

            @Override
            public void recordError(Message message, TunnelTip tip) {
                messageRepository.updateMessageStatusAndTryTimes(message.getId(), PushStatusEnum.FAILED, message.getTryTimes().get());
                messagePushRepository.updateMessagePushStatus(message.getId(), PushStatusEnum.FAILED, tip.getCause(), tip.getTip());
            }

            @Override
            public void recordSuccess(Message message, TunnelTip tip) {
                messageRepository.updateMessageStatusAndTryTimes(message.getId(), PushStatusEnum.SUCCESS, message.getTryTimes().get());
                messagePushRepository.updateMessagePushStatus(message.getId(), PushStatusEnum.SUCCESS, tip.getCause(), tip.getTip());
            }

            @Override
            public void recordRetry(Message message, TunnelTip tip) {
                messageRepository.updateMessageStatusAndTryTimes(message.getId(), PushStatusEnum.RETRYING, message.getTryTimes().get());
                messagePushRepository.updateMessagePushStatus(message.getId(), PushStatusEnum.RETRYING, tip.getCause(), tip.getTip());
            }
        };
    }

    protected PushLock buildPushLock() {
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

    protected ReceiverFactory buildReceiverFactory() {
        return new ReceiverFactory() {
            @Override
            public List<Receiver> substances(String tag) {
                return receiverRepository.substanceNames(tag).stream().map(Receiver::new).collect(Collectors.toList());
            }

            @Override
            public void register(Receiver substance, String tag) {
                receiverRepository.register(substance, tag);
            }

            @Override
            public boolean nameInTag(String tag, String name) {
                return receiverRepository.nameInTag(tag, name);
            }

            @Override
            public Receiver getSubstanceByName(String name) {
                return new Receiver(name);
            }
        };
    }

    protected BusinessFactory buildBusinessFactory() {
        return new BusinessFactory() {
            @Override
            public List<Business> substances(String tag) {
                return businessRepository.substanceNames(tag).stream().map(Business::new).collect(Collectors.toList());
            }

            @Override
            public void register(Business substance, String tag) {
                businessRepository.register(substance, tag);
            }

            @Override
            public boolean nameInTag(String tag, String name) {
                return businessRepository.nameInTag(tag, name);
            }

            @Override
            public Business getSubstanceByName(String name) {
                return new Business(name);
            }
        };
    }

    protected TunnelFactory buildTunnelFactory() {
        return new TunnelFactory() {
            @Override
            public List<AbstractTunnel> substances(String tag) {
                return tunnelRepository.substanceNames(tag).stream().map(this::getSubstanceByName).collect(Collectors.toList());
            }

            @Override
            public void register(AbstractTunnel substance, String tag) {
                tunnelRepository.register(substance, tag);
            }

            @Override
            public boolean nameInTag(String tag, String name) {
                return tunnelRepository.nameInTag(tag, name);
            }

            @Override
            public AbstractTunnel getSubstanceByName(String name) {
                return tunnelRepository.getByName(name);
            }
        };
    }

    protected MessageDetail buildMessageDetail(Message message, Receiver receiver, AbstractTunnel tunnel, boolean head) {
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setMessageId(message.getId());
        messageDetail.setBiz(message.getBiz().getName());
        messageDetail.setCreateTime(LocalDateTime.now());
        messageDetail.setData(message.getData());
        messageDetail.setHead(head);
        messageDetail.setTryTimes(message.getTryTimes().get());
        messageDetail.setPolicy(message.getPolicy());
        messageDetail.setReceiver(receiver.getName());
        messageDetail.setStatus(PushStatusEnum.CREATED);
        messageDetail.setTunnel(tunnel.getName());
        return messageDetail;
    }

    protected MessagePush buildMessagePush(MessageDetail messageDetail) {
        MessagePush messagePush = new MessagePush();
        messagePush.setBiz(messageDetail.getBiz());
        messagePush.setCreateTime(messageDetail.getCreateTime());
        messagePush.setMessageId(messageDetail.getId());
        messagePush.setDuplex(messageDetail.getPolicy().getTunnelPolicy().isDuplex());
        messagePush.setFollowSuggestion(messageDetail.getPolicy().getRetryPolicy().isFollowSuggestion());
        messagePush.setOrdered(messageDetail.getPolicy().getTunnelPolicy().isOrdered());
        messagePush.setReceiver(messageDetail.getReceiver());
        messagePush.setRetry(messageDetail.getPolicy().getRetryPolicy().getRetry());
        messagePush.setRetryDuplex(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().isDuplex());
        messagePush.setRetryOrdered(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().isOrdered());
        messagePush.setRetryStateful(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().isStateful());
        messagePush.setRetryTimeoutMills(messageDetail.getPolicy().getRetryPolicy().getTunnelPolicy().getTimeoutMills());
        PushTrigger retryTrigger = messageDetail.getPolicy().getRetryPolicy().getTrigger();
        messagePush.setRetryTriggerTime(retryTrigger instanceof ScheduleTrigger ? ((ScheduleTrigger) retryTrigger).getSchedule() : LocalDateTime.now());
        messagePush.setStateful(messageDetail.getPolicy().getTunnelPolicy().isStateful());
        messagePush.setStatus(PushStatusEnum.CREATED);
        messagePush.setTimeoutMills(messageDetail.getPolicy().getTunnelPolicy().getTimeoutMills());
        PushTrigger pushTrigger = messageDetail.getPolicy().getTrigger();
        messagePush.setTriggerTime(pushTrigger instanceof ScheduleTrigger ? ((ScheduleTrigger) pushTrigger).getSchedule() : LocalDateTime.now());
        messagePush.setTryTimes(messageDetail.getTryTimes());
        messagePush.setTunnel(messageDetail.getTunnel());
        return messagePush;
    }

    protected Message buildMessage(MessageDetail messageDetail) {
        Message message = new Message(
                messageDetail.getId(),
                new Business(messageDetail.getBiz()),
                messageDetail.getData(),
                messageDetail.getPolicy());
        message.initTried(messageDetail.getTryTimes());
        return message;
    }
}
