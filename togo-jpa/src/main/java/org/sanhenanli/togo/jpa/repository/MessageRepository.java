package org.sanhenanli.togo.jpa.repository;

import org.sanhenanli.togo.jpa.model.MessageEntity;
import org.springframework.data.repository.CrudRepository;


/**
 * datetime 2020/9/25 15:59
 *
 * @author zhouwenxiang
 */
public interface MessageRepository extends CrudRepository<MessageEntity, Integer> {

    MessageEntity findByMessageIdAndReceiverAndTunnelAndBiz(String messageId, String receiver, String tunnel, String biz);

}
