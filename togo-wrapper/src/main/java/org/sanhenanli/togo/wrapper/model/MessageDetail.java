package org.sanhenanli.togo.wrapper.model;

import lombok.Data;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/22 19:06
 *
 * @author zhouwenxiang
 */
@Data
public class MessageDetail {

    protected String detailId;
    protected String messageId;
    protected String data;
    protected String receiver;
    protected String tunnel;
    protected String biz;
    protected boolean head;
    protected int tryTime;
    protected RetryablePushPolicy policy;
    protected PushStatusEnum status;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;
    protected LocalDateTime receiptTime;
}
