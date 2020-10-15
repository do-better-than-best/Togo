package org.sanhenanli.togo.jpa.repository;

import org.sanhenanli.togo.jpa.model.MessageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * datetime 2020/9/25 15:59
 *
 * @author zhouwenxiang
 */
@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, Integer> {

    MessageEntity findByMessageIdAndReceiverAndTunnelAndBiz(String messageId, String receiver, String tunnel, String biz);

}
