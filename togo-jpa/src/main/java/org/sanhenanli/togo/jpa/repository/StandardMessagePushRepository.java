package org.sanhenanli.togo.jpa.repository;

import org.sanhenanli.togo.jpa.model.PushRecordEntity;
import org.sanhenanli.togo.network.pusher.PusherIdentity;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;
import org.sanhenanli.togo.api.model.MessagePush;
import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.sanhenanli.togo.api.repository.MessagePushRepository;
import org.sanhenanli.togo.api.repository.TunnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * datetime 2020/3/4 12:05
 *
 * @author zhouwenxiang
 */
@Transactional(rollbackFor = Exception.class)
@Repository
public class StandardMessagePushRepository implements MessagePushRepository {

    @Autowired
    private PushRecordRepository pushRecordRepository;

    @Autowired
    private TunnelRepository tunnelRepository;

    @Override
    public void saveMessagePush(MessagePush messagePush, boolean head) {
        if (head) {
            // 如果是优先消息, 设置pushOrder
            String receiver = messagePush.getReceiver();
            String tunnel = messagePush.getTunnel();
            // 查找receiver*tunnel的最优先待推的pushOrder
            PushRecordEntity entity = pushRecordRepository
                    .findFirstByReceiverAndTunnelAndStatusInOrderByPushOrderAsc(
                            receiver, tunnel, PushStatusEnum.unfinished().stream().map(PushStatusEnum::getStatus).collect(Collectors.toList())
                    );
            Long pushOrder = entity == null ? null : entity.getPushOrder();
            messagePush.setPushOrder(pushOrder == null ? Long.MAX_VALUE : pushOrder - 1);
        }
        pushRecordRepository.save(PushRecordEntity.parse(messagePush));
    }

    @Override
    public void updateMessagePushStatus(String messageId, PushStatusEnum status, TunnelTipCauseEnum cause, String tip) {
        PushRecordEntity entity = pushRecordRepository.findFirstByMessageIdOrderByIdDesc(messageId);
        assert entity != null;
        entity.setFinishTime(LocalDateTime.now());
        entity.setStatus(status.getStatus());
        entity.setTip(tip);
        if (cause != null) {
            entity.setCause(cause.getCause());
        }
        pushRecordRepository.save(entity);
    }

    @Override
    public void updateStatusByReceiverAndTunnelAndStatusIn(String receiver, String tunnel, PushStatusEnum status, Collection<PushStatusEnum> statusCollection) {
        pushRecordRepository.updateStatusByReceiverAndTunnelAndStatusIn(receiver, tunnel, status.getStatus(), statusCollection.stream().map(PushStatusEnum::getStatus).collect(Collectors.toList()));
    }

    @Override
    public LocalDateTime findRecentFinishTimeByReceiverAndBizAndTunnelAndStatusIn(long number, Set<String> receivers, Set<String> bizs, Set<String> tunnels, Set<PushStatusEnum> status) {
        Specification<PushRecordEntity> specification = (Specification<PushRecordEntity>) (root, criteriaQuery, criteriaBuilder) -> root.get("status").in(status.stream().map(PushStatusEnum::getStatus).collect(Collectors.toList()));
        if (receivers != null && !receivers.isEmpty()) {
            specification.and((root, criteriaQuery, criteriaBuilder) -> root.get("receiver").in(receivers));
        }
        if (tunnels != null && !tunnels.isEmpty()) {
            specification.and((root, criteriaQuery, criteriaBuilder) -> root.get("tunnel").in(tunnels));
        }
        if (bizs != null && !bizs.isEmpty()) {
            specification.and((root, criteriaQuery, criteriaBuilder) -> root.get("biz").in(bizs));
        }
        Page<PushRecordEntity> entities = pushRecordRepository.findAll(specification, PageRequest.of((int) (number - 1), 1, Sort.Direction.DESC, "finishTime"));
        if (entities.isEmpty() || entities.getSize() < number) {
            return null;
        }
        return entities.getContent().get(0).getFinishTime();
    }

    @Override
    public MessagePush popByReceiverAndTunnelAndOrderedAndStatefulAndStatusIsUnfinishedOrderByPushOrder(String receiver, String tunnel, boolean ordered, boolean stateful) {
        List<Integer> status = Collections.singletonList(PushStatusEnum.CREATED.getStatus());
        PushRecordEntity entity = pushRecordRepository
                .findFirstByReceiverAndTunnelAndOrderedAndStatefulAndStatusInOrderByPushOrderAsc(
                        receiver, tunnel, ordered, stateful, status
                );
        if (entity == null) {
            return null;
        }
        // 修改状态为推送中
        entity.setStatus(PushStatusEnum.PUSHING.getStatus());
        entity.setPushTime(LocalDateTime.now());
        pushRecordRepository.save(entity);
        return entity.extract();
    }

    @Override
    public MessagePush popByReceiverAndTunnelAndOrderedAndStatusIsUnfinishedOrderByPushOrder(String receiver, String tunnel, boolean ordered) {
        List<Integer> status = Collections.singletonList(PushStatusEnum.CREATED.getStatus());
        PushRecordEntity entity = pushRecordRepository
                .findFirstByReceiverAndTunnelAndOrderedAndStatusInOrderByPushOrderAsc(
                        receiver, tunnel, ordered, status
                );
        if (entity == null) {
            return null;
        }
        // 修改状态为推送中
        entity.setStatus(PushStatusEnum.PUSHING.getStatus());
        entity.setPushTime(LocalDateTime.now());
        pushRecordRepository.save(entity);
        return entity.extract();
    }

    @Override
    public Set<PusherIdentity> findDistinctReceiverAndTunnelByStatusIsUnfinished() {
        List<Object[]> list = pushRecordRepository
                .findDistinctReceiverAndTunnelAndStatusIn(
                        PushStatusEnum.unfinished().stream().map(PushStatusEnum::getStatus).collect(Collectors.toSet())
                );
        return list.stream().map(t -> new PusherIdentity(new Receiver((String) t[0]), tunnelRepository.getByName((String) t[1]))).collect(Collectors.toSet());
    }
}
