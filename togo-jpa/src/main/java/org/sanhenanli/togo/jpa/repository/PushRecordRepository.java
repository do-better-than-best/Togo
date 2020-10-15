package org.sanhenanli.togo.jpa.repository;

import org.sanhenanli.togo.jpa.model.PushRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * datetime 2020/9/25 15:59
 *
 * @author zhouwenxiang
 */
@Repository
public interface PushRecordRepository extends JpaRepository<PushRecordEntity, Integer>, JpaSpecificationExecutor<PushRecordEntity> {

    PushRecordEntity findFirstByMessageIdOrderByIdDesc(String messageId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update PushRecordEntity e set e.status = :status where e.receiver = :receiver and e.tunnel = :tunnel and e.status in :statusCollection")
    void updateStatusByReceiverAndTunnelAndStatusIn(@Param("receiver") String receiver, @Param("tunnel") String tunnel, @Param("status") int status, @Param("statusCollection") Collection<Integer> statusCollection);

    PushRecordEntity findFirstByReceiverAndTunnelAndOrderedAndStatefulAndStatusInOrderByPushOrderAsc(String receiver, String tunnel, boolean ordered, boolean stateful, Collection<Integer> status);

    PushRecordEntity findFirstByReceiverAndTunnelAndOrderedAndStatusInOrderByPushOrderAsc(String receiver, String tunnel, boolean ordered, Collection<Integer> status);

    PushRecordEntity findFirstByReceiverAndTunnelAndStatusInOrderByPushOrderAsc(String receiver, String tunnel, Collection<Integer> status);

    @Query("select distinct e.receiver, e.tunnel from PushRecordEntity e where e.status in :status")
    List<Object[]> findDistinctReceiverAndTunnelAndStatusIn(@Param("status") Collection<Integer> status);

}
