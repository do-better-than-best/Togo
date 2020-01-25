package org.sanhenanli.togo.wrapper.repository;

import org.sanhenanli.togo.wrapper.model.MessageDetail;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;

/**
 * datetime 2020/1/22 18:58
 * 消息管理
 *
 * @author zhouwenxiang
 */
public interface MessageRepository {

    /**
     * 保存一条消息, 对messageId*biz去重
     * @param message 消息
     * @return 消息, 打上了唯一id
     */
    MessageDetail save(MessageDetail message);

    /**
     * 更新消息的尝试次数
     * @param id 消息唯一id
     * @param tryTimes 尝试次数
     */
    void updateMessageTryTimes(String id, int tryTimes);

    /**
     * 更新消息的推送状态
     * @param id 消息唯一id
     * @param status 推送状态
     */
    void updateMessageStatus(String id, PushStatusEnum status);

    /**
     * 查询消息详情
     * @param id 消息唯一id
     * @return 消息
     */
    MessageDetail findOne(String id);

    /**
     * 保存消息回执: 即更新消息的回执时间
     * @param id 消息唯一id
     */
    void saveMessageReceipt(String id);

    /**
     * 查询消息回执: 即判断消息是否有回执时间
     * @param id 消息唯一id
     * @return true有消息回执
     */
    boolean hasReceipt(String id);
}
