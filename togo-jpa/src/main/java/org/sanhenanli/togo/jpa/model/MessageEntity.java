package org.sanhenanli.togo.jpa.model;

import com.alibaba.fastjson.JSONObject;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.api.model.MessageDetail;
import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * datetime 2020/9/25 15:56
 *
 * @author zhouwenxiang
 */
@Entity
@Table(name = "message", schema = "togo", catalog = "")
public class MessageEntity {
    private Integer id;
    private String messageId;
    private String data;
    private String receiver;
    private String tunnel;
    private String biz;
    private Boolean head;
    private Integer tryTimes;
    private String policy;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime receiptTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "message_id")
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Basic
    @Column(name = "receiver")
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Basic
    @Column(name = "tunnel")
    public String getTunnel() {
        return tunnel;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
    }

    @Basic
    @Column(name = "biz")
    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    @Basic
    @Column(name = "head")
    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    @Basic
    @Column(name = "try_times")
    public Integer getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    @Basic
    @Column(name = "policy")
    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_time", insertable = false, updatable = false, columnDefinition = "CURRENT_TIMESTAMP")
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "receipt_time")
    public LocalDateTime getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(LocalDateTime receiptTime) {
        this.receiptTime = receiptTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return id == that.id &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(data, that.data) &&
                Objects.equals(receiver, that.receiver) &&
                Objects.equals(tunnel, that.tunnel) &&
                Objects.equals(biz, that.biz) &&
                Objects.equals(head, that.head) &&
                Objects.equals(tryTimes, that.tryTimes) &&
                Objects.equals(policy, that.policy) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(receiptTime, that.receiptTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageId, data, receiver, tunnel, biz, head, tryTimes, policy, status, createTime, updateTime, receiptTime);
    }

    /**
     * 从messageDetail转化
     * @param messageDetail messageDetail
     * @return this
     */
    public static MessageEntity parse(MessageDetail messageDetail) {
        MessageEntity entity = new MessageEntity();
        BeanUtils.copyProperties(messageDetail, entity);
        entity.setId(messageDetail.getId() == null ? null : Integer.valueOf(messageDetail.getId()));
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
        messageDetail.setId(String.valueOf(this.getId()));
        messageDetail.setStatus(PushStatusEnum.getByStatus(this.status));
        messageDetail.setPolicy(JSONObject.parseObject(this.policy, RetryablePushPolicy.class));
        return messageDetail;
    }
}
