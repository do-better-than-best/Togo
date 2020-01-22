package org.sanhenanli.togo.wrapper.message;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/22 19:06
 *
 * @author zhouwenxiang
 */
public class MessageWrapper {

    protected String messageId;
    protected String data;
    protected String receiver;
    protected String tunnel;
    protected String biz;

    // todo 失败要重试的怎么记录: 入库, 检查同id,biz下tryTimes=0的重复;  pop哪些状态的消息:发送中的唯一id
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
    protected int status;
    protected int cause;
    protected String tip;
    protected LocalDateTime suggestTime;

    protected LocalDateTime createTime;
    protected LocalDateTime pushTime;
    protected LocalDateTime finishTime;
}
