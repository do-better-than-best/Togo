package org.sanhenanli.togo.wrapper.repository;

import org.sanhenanli.togo.wrapper.model.MessageDetail;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;

/**
 * datetime 2020/1/22 18:58
 *
 * @author zhouwenxiang
 */
public interface MessageRepository {

    void save(MessageDetail message); // 检查尝试次数

    MessageDetail findOne(String id);

    void saveMessageReceipt(String id);

    boolean hasReceipt(String id);

    void updateMessageStatus(String id, PushStatusEnum status);
}
