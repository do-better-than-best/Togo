package org.sanhenanli.togo.application.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.api.model.MessageDetail;
import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/25 15:36
 * 消息实体
 *
 * @author zhouwenxiang
 */
@Data
public class MessageEntity {

    /**
     * 消息唯一id
     */
    protected String id;
    /**
     * 消息在业务中的id
     */
    protected String messageId;
    /**
     * 要推送的消息数据
     */
    protected String data;
    /**
     * 接收者
     */
    protected String receiver;
    /**
     * 要使用的通道
     */
    protected String tunnel;
    /**
     * 消息所属业务
     */
    protected String biz;
    /**
     * 是否优先推送
     */
    protected boolean head;
    /**
     * 尝试次数
     */
    protected int tryTime;
    /**
     * 推送策略
     */
    protected String policy;
    /**
     * 推送状态
     */
    protected int status;
    /**
     * 创建时间
     */
    protected LocalDateTime createTime;
    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
    /**
     * 上报回执时间
     */
    protected LocalDateTime receiptTime;

    /**
     * 从messageDetail转化
     * @param messageDetail messageDetail
     * @return this
     */
    public static MessageEntity parse(MessageDetail messageDetail) {
        MessageEntity entity = new MessageEntity();
        BeanUtils.copyProperties(messageDetail, entity);
        entity.setStatus(messageDetail.getStatus().getStatus());
        entity.setPolicy(JSONObject.toJSONString(messageDetail.getPolicy()));
        return entity;
    }

    /**
     * 提取出messageDetail
     * @return messageDetail
     */
    public MessageDetail extract() {
        MessageDetail messageDetail = new MessageDetail();
        BeanUtils.copyProperties(this, messageDetail);
        messageDetail.setStatus(PushStatusEnum.getByStatus(this.status));
        messageDetail.setPolicy(JSONObject.parseObject(this.policy, RetryablePushPolicy.class));
        return messageDetail;
    }
}
