package org.sanhenanli.togo.jpa.model;

import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;
import org.sanhenanli.togo.api.model.MessagePush;
import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * datetime 2020/9/25 15:56
 *
 * @author zhouwenxiang
 */
@Entity
@Table(name = "push_record", schema = "togo", catalog = "")
public class PushRecordEntity {
    private Integer id;
    private String messageId;
    private String receiver;
    private String tunnel;
    private String biz;
    private Long pushOrder;
    private Boolean stateful;
    private Boolean duplex;
    private Boolean ordered;
    private Long timeoutMills;
    private LocalDateTime triggerTime;
    private Boolean retryStateful;
    private Boolean retryDuplex;
    private Boolean retryOrdered;
    private Long retryTimeoutMills;
    private Integer retry;
    private Boolean followSuggestion;
    private LocalDateTime retryTriggerTime;
    private Integer tryTimes;
    private Integer status;
    private Integer cause;
    private String tip;
    private LocalDateTime suggestTime;
    private LocalDateTime createTime;
    private LocalDateTime pushTime;
    private LocalDateTime finishTime;

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
    @Column(name = "push_order")
    public Long getPushOrder() {
        return pushOrder;
    }

    public void setPushOrder(Long pushOrder) {
        this.pushOrder = pushOrder;
    }

    @Basic
    @Column(name = "stateful")
    public Boolean getStateful() {
        return stateful;
    }

    public void setStateful(Boolean stateful) {
        this.stateful = stateful;
    }

    @Basic
    @Column(name = "duplex")
    public Boolean getDuplex() {
        return duplex;
    }

    public void setDuplex(Boolean duplex) {
        this.duplex = duplex;
    }

    @Basic
    @Column(name = "ordered")
    public Boolean getOrdered() {
        return ordered;
    }

    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }

    @Basic
    @Column(name = "timeout_mills")
    public Long getTimeoutMills() {
        return timeoutMills;
    }

    public void setTimeoutMills(Long timeoutMills) {
        this.timeoutMills = timeoutMills;
    }

    @Basic
    @Column(name = "trigger_time")
    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(LocalDateTime triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Basic
    @Column(name = "retry_stateful")
    public Boolean getRetryStateful() {
        return retryStateful;
    }

    public void setRetryStateful(Boolean retryStateful) {
        this.retryStateful = retryStateful;
    }

    @Basic
    @Column(name = "retry_duplex")
    public Boolean getRetryDuplex() {
        return retryDuplex;
    }

    public void setRetryDuplex(Boolean retryDuplex) {
        this.retryDuplex = retryDuplex;
    }

    @Basic
    @Column(name = "retry_ordered")
    public Boolean getRetryOrdered() {
        return retryOrdered;
    }

    public void setRetryOrdered(Boolean retryOrdered) {
        this.retryOrdered = retryOrdered;
    }

    @Basic
    @Column(name = "retry_timeout_mills")
    public Long getRetryTimeoutMills() {
        return retryTimeoutMills;
    }

    public void setRetryTimeoutMills(Long retryTimeoutMills) {
        this.retryTimeoutMills = retryTimeoutMills;
    }

    @Basic
    @Column(name = "retry")
    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    @Basic
    @Column(name = "follow_suggestion")
    public Boolean getFollowSuggestion() {
        return followSuggestion;
    }

    public void setFollowSuggestion(Boolean followSuggestion) {
        this.followSuggestion = followSuggestion;
    }

    @Basic
    @Column(name = "retry_trigger_time")
    public LocalDateTime getRetryTriggerTime() {
        return retryTriggerTime;
    }

    public void setRetryTriggerTime(LocalDateTime retryTriggerTime) {
        this.retryTriggerTime = retryTriggerTime;
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
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "cause")
    public Integer getCause() {
        return cause;
    }

    public void setCause(Integer cause) {
        this.cause = cause;
    }

    @Basic
    @Column(name = "tip")
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Basic
    @Column(name = "suggest_time")
    public LocalDateTime getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(LocalDateTime suggestTime) {
        this.suggestTime = suggestTime;
    }

    @Basic
    @Column(name = "create_time")
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "push_time")
    public LocalDateTime getPushTime() {
        return pushTime;
    }

    public void setPushTime(LocalDateTime pushTime) {
        this.pushTime = pushTime;
    }

    @Basic
    @Column(name = "finish_time")
    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PushRecordEntity that = (PushRecordEntity) o;
        return id == that.id &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(receiver, that.receiver) &&
                Objects.equals(tunnel, that.tunnel) &&
                Objects.equals(biz, that.biz) &&
                Objects.equals(pushOrder, that.pushOrder) &&
                Objects.equals(stateful, that.stateful) &&
                Objects.equals(duplex, that.duplex) &&
                Objects.equals(ordered, that.ordered) &&
                Objects.equals(timeoutMills, that.timeoutMills) &&
                Objects.equals(triggerTime, that.triggerTime) &&
                Objects.equals(retryStateful, that.retryStateful) &&
                Objects.equals(retryDuplex, that.retryDuplex) &&
                Objects.equals(retryOrdered, that.retryOrdered) &&
                Objects.equals(retryTimeoutMills, that.retryTimeoutMills) &&
                Objects.equals(retry, that.retry) &&
                Objects.equals(followSuggestion, that.followSuggestion) &&
                Objects.equals(retryTriggerTime, that.retryTriggerTime) &&
                Objects.equals(tryTimes, that.tryTimes) &&
                Objects.equals(status, that.status) &&
                Objects.equals(cause, that.cause) &&
                Objects.equals(tip, that.tip) &&
                Objects.equals(suggestTime, that.suggestTime) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(pushTime, that.pushTime) &&
                Objects.equals(finishTime, that.finishTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageId, receiver, tunnel, biz, pushOrder, stateful, duplex, ordered, timeoutMills, triggerTime, retryStateful, retryDuplex, retryOrdered, retryTimeoutMills, retry, followSuggestion, retryTriggerTime, tryTimes, status, cause, tip, suggestTime, createTime, pushTime, finishTime);
    }

    public static PushRecordEntity parse(MessagePush messagePush) {
        PushRecordEntity entity = new PushRecordEntity();
        BeanUtils.copyProperties(messagePush, entity);
        entity.setId(StringUtils.isEmpty(messagePush.getId()) ? null : Integer.parseInt(messagePush.getId()));
        entity.setStatus(messagePush.getStatus().getStatus());
        entity.setCause(messagePush.getCause() == null ? null : messagePush.getCause().getCause());
        return entity;
    }

    public MessagePush extract() {
        MessagePush messagePush = new MessagePush();
        BeanUtils.copyProperties(this, messagePush);
        messagePush.setId(String.valueOf(this.id));
        messagePush.setStatus(PushStatusEnum.getByStatus(this.status));
        messagePush.setCause(this.cause == null ? null : TunnelTipCauseEnum.getByCause(this.cause));
        return messagePush;
    }
}
