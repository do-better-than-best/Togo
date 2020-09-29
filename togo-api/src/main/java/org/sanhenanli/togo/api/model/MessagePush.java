package org.sanhenanli.togo.api.model;

import lombok.Data;
import org.sanhenanli.togo.api.model.enums.PushStatusEnum;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/24 15:09
 * 消息推送的结构
 *
 * @author zhouwenxiang
 */
@Data
public class MessagePush {

    /**
     * 此次推送的id
     */
    protected String id;
    /**
     * 消息唯一id
     */
    protected String messageId;
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
     * 推送顺序, 越小越优先
     */
    protected Long pushOrder;

    /** 推送策略 **/
    protected boolean stateful;
    protected boolean duplex;
    protected boolean ordered;
    protected long timeoutMills;
    protected LocalDateTime triggerTime;

    /** 重试策略 **/
    protected boolean retryStateful;
    protected boolean retryDuplex;
    protected boolean retryOrdered;
    protected long retryTimeoutMills;
    protected int retry;
    protected boolean followSuggestion;
    protected LocalDateTime retryTriggerTime;

    /**
     * 已尝试次数
     */
    protected int tryTimes;
    /**
     * 推送状态
     */
    protected PushStatusEnum status;
    /**
     * 推送失败原因
     */
    protected TunnelTipCauseEnum cause;
    /**
     * 推送失败描述
     */
    protected String tip;
    /**
     * 推送失败后的建议重试时间
     */
    protected LocalDateTime suggestTime;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;
    /**
     * 开始推送时间
     */
    protected LocalDateTime pushTime;
    /**
     * 推送结束时间
     */
    protected LocalDateTime finishTime;
}
