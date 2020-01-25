package org.sanhenanli.togo.application.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.wrapper.model.MessageDetail;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/25 15:36
 *
 * @author zhouwenxiang
 */
@Data
public class MessageEntity {

    protected String id;
    protected String messageId;
    protected String data;
    protected String receiver;
    protected String tunnel;
    protected String biz;
    protected boolean head;
    protected int tryTime;
    protected String policy;
    protected int status;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;
    protected LocalDateTime receiptTime;

    public static MessageEntity parse(MessageDetail messageDetail) {
        MessageEntity entity = new MessageEntity();
        BeanUtils.copyProperties(messageDetail, entity);
        entity.setStatus(messageDetail.getStatus().getStatus());
        entity.setPolicy(JSONObject.toJSONString(messageDetail.getPolicy()));
        return entity;
    }

    public MessageDetail extract() {
        MessageDetail messageDetail = new MessageDetail();
        BeanUtils.copyProperties(this, messageDetail);
        messageDetail.setStatus(PushStatusEnum.getByStatus(this.status));
        messageDetail.setPolicy(JSONObject.parseObject(this.policy, RetryablePushPolicy.class));
        return messageDetail;
    }
}
