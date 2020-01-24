package org.sanhenanli.togo.wrapper.model;

import lombok.Data;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;
import org.sanhenanli.togo.wrapper.model.enums.PushStatusEnum;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/24 15:09
 *
 * @author zhouwenxiang
 */
@Data
public class MessagePush {

    protected String pushId;
    protected String detailId;
    protected String receiver;
    protected String tunnel;
    protected String biz;
    protected Long pushOrder;

    protected boolean stateful;
    protected boolean duplex;
    protected boolean ordered;
    protected long timeoutMills;
    protected LocalDateTime triggerTime;

    protected boolean retryStateful;
    protected boolean retryDuplex;
    protected boolean retryOrdered;
    protected long retryTimeoutMills;
    protected int retry;
    protected boolean followSuggestiong;
    protected LocalDateTime retryTriggerTime;

    protected int tryTimes;
    protected PushStatusEnum status;
    protected TunnelTipCauseEnum cause;
    protected String tip;
    protected LocalDateTime suggestTime;

    protected LocalDateTime createTime;
    protected LocalDateTime pushTime;
    protected LocalDateTime finishTime;
}
