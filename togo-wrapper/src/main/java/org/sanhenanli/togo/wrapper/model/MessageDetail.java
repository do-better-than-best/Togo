package org.sanhenanli.togo.wrapper.model;

import lombok.Data;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/22 19:06
 * 消息完整结构
 *
 * @author zhouwenxiang
 */
@Data
public class MessageDetail {

    /**
     * 消息唯一id
     */
    protected String id;
    /**
     * 消息的业务id
     */
    protected String messageId;
    /**
     * 要推送的消息数据
     */
    protected String data;
    /**
     * 消息接收者
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
    protected RetryablePushPolicy policy;
    /**
     * 推送状态
     */
    protected PushStatusEnum status;
    /**
     * 创建时间
     */
    protected LocalDateTime createTime;
    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
    /**
     * 收到回执的时间
     */
    protected LocalDateTime receiptTime;
}
