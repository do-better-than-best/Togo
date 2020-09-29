package org.sanhenanli.togo.jpa.repository;

import org.sanhenanli.togo.jpa.model.MessageEntity;
import org.sanhenanli.togo.api.model.MessageDetail;
import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.sanhenanli.togo.api.repository.MessageRepository;
import org.sanhenanli.togo.network.trigger.InstantlyTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * datetime 2020/3/4 11:05
 *
 * @author zhouwenxiang
 */
@Transactional(rollbackFor = Exception.class)
@Repository
public class StandardMessageRepository implements MessageRepository {

    @Autowired
    private org.sanhenanli.togo.jpa.repository.MessageRepository messageRepository;

    @Override
    public MessageDetail save(MessageDetail message) {
        MessageEntity entity = MessageEntity.parse(message);
        if (!StringUtils.isEmpty(message.getMessageId())) {
            MessageEntity exists = messageRepository.findByMessageIdAndReceiverAndTunnelAndBiz(message.getMessageId(), message.getReceiver(), message.getTunnel(), message.getBiz());
            if (exists != null) {
                // 已经提交过此消息, 不重复提交
                return null;
            }
        }
        messageRepository.save(entity);
        message.setId(String.valueOf(entity.getId()));
        return message;
    }

    @Override
    public void updateMessageStatusAndTryTimes(String id, PushStatusEnum status, int tryTimes) {
        Optional<MessageEntity> entityOp = messageRepository.findById(Integer.valueOf(id));
        assert entityOp.isPresent();
        MessageEntity entity = entityOp.get();
        entity.setStatus(status.getStatus());
        entity.setTryTimes(tryTimes);
        messageRepository.save(entity);
    }

    @Override
    public MessageDetail findOne(String id) {
        Optional<MessageEntity> entityOp = messageRepository.findById(Integer.valueOf(id));
        assert entityOp.isPresent();
        return entityOp.get().extract();
    }

    @Override
    public void saveMessageReceipt(String messageId, String receiver, String tunnel, String biz) {
        MessageEntity entity = messageRepository.findByMessageIdAndReceiverAndTunnelAndBiz(messageId, receiver, tunnel, biz);
        if (entity != null) {
            entity.setReceiptTime(LocalDateTime.now());
            messageRepository.save(entity);
        }
    }

    @Override
    public boolean hasReceipt(String id) {
        return findOne(id).getReceiptTime() != null;
    }
}
