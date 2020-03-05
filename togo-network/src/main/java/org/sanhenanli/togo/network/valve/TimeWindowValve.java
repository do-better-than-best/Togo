package org.sanhenanli.togo.network.valve;

import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.receiver.Receiver;
import org.sanhenanli.togo.network.rule.AbstractRule;
import org.sanhenanli.togo.network.rule.TimeWindowRule;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * datetime 2020/1/22 11:29
 * 时间窗口推送控制阀门: 控制一段时间窗口内的推送次数
 *
 * @author zhouwenxiang
 */
public class TimeWindowValve extends AbstractValve {

    public TimeWindowValve(AbstractRule rule) {
        super(rule);
    }

    @Override
    public ValveTip control(Receiver receiver, Message message, AbstractTunnel tunnel) {
        TimeWindowRule rule = (TimeWindowRule) this.rule;
        if (rule.getQuota() <= 0 || rule.getMills() <= 0) {
            return ValveTip.ok();
        }
        if (rule.getPushScope().isSuccess()) {
            if (rule.getBizScope().isAllBiz()) {
                if (rule.getReceiverScope().isAllReceiver()) {
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, null, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("%d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, null, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("%d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    // todo 补全时间窗口阀门逻辑
                }
            }
        }
        return ValveTip.ok();
    }

    @Override
    public boolean support(AbstractRule rule) {
        return rule instanceof TimeWindowRule;
    }

}
