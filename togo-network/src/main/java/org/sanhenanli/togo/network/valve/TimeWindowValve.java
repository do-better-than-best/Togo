package org.sanhenanli.togo.network.valve;

import org.sanhenanli.togo.network.business.Business;
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
        ValveTip valveTip;
        if (rule.getPushScope().isSuccess()) {
            valveTip = controlSuccess(receiver, message, tunnel, rule);
            if (!valveTip.isOk()) {
                return valveTip;
            }
        }
        if (rule.getPushScope().isAttempt()) {
            valveTip = controlAttempt(receiver, message, tunnel, rule);
            if (!valveTip.isOk()) {
                return valveTip;
            }
        }
        if (rule.getPushScope().isError()) {
            valveTip = controlError(receiver, message, tunnel, rule);
            if (!valveTip.isOk()) {
                return valveTip;
            }
        }
        return ValveTip.ok();
    }

    private ValveTip controlSuccess(Receiver receiver, Message message, AbstractTunnel tunnel, TimeWindowRule rule) {
        if (rule.getBizScope().isAllBiz()) {
            if (rule.getReceiverScope().isAllReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, null, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aaa success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, null, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aae success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("aas success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isEachReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, null, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aea success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, null, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aee success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("aes success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isSpecificReceiver()) {
                String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                if (specificReceiver != null) {
                    Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, null, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("asa success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, null, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ase success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ass success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
            }
        }
        if (rule.getBizScope().isEachBiz()) {
            Business biz = message.getBiz();
            if (rule.getReceiverScope().isAllReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, biz, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eaa success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, biz, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eae success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("eas success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isEachReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, biz, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eea success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, biz, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eee success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ees success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isSpecificReceiver()) {
                String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                if (specificReceiver != null) {
                    Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("esa success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ese success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ess success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
            }
        }
        if (rule.getBizScope().isSpecificBiz()) {
            String bizStr = bizToControl(message, rule.getBizScope());
            if (bizStr != null) {
                Business biz = this.pusher.getBusinessFactory().getSubstanceByName(bizStr);
                if (rule.getReceiverScope().isAllReceiver()) {
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("saa success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("sae success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), null, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("sas success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
                if (rule.getReceiverScope().isEachReceiver()) {
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("sea success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("see success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), receiver, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ses success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
                if (rule.getReceiverScope().isSpecificReceiver()) {
                    String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                    if (specificReceiver != null) {
                        Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                        if (rule.getTunnelScope().isAllTunnel()) {
                            LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, biz, null);
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ssa success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                        if (rule.getTunnelScope().isEachTunnel()) {
                            LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, biz, tunnel);
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("sse success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                        if (rule.getTunnelScope().isSpecificTunnel()) {
                            String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                            if (specificTunnel != null) {
                                LocalDateTime time = pusher.getRecorder().lastSuccessTime(rule.getQuota(), specificReceiverObj, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                                if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                    LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                    return ValveTip.block(String.format("sss success %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                            suggestTime);
                                }
                            }
                        }
                    }
                }
            }
        }
        return ValveTip.ok();
    }

    private ValveTip controlAttempt(Receiver receiver, Message message, AbstractTunnel tunnel, TimeWindowRule rule) {
        if (rule.getBizScope().isAllBiz()) {
            if (rule.getReceiverScope().isAllReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, null, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aaa attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, null, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aae attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("aas attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isEachReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, null, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aea attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, null, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aee attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("aes attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isSpecificReceiver()) {
                String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                if (specificReceiver != null) {
                    Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, null, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("asa attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, null, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ase attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ass attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
            }
        }
        if (rule.getBizScope().isEachBiz()) {
            Business biz = message.getBiz();
            if (rule.getReceiverScope().isAllReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, biz, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eaa attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, biz, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eae attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("eas attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isEachReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, biz, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eea attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, biz, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eee attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ees attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isSpecificReceiver()) {
                String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                if (specificReceiver != null) {
                    Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("esa attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ese attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ess attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
            }
        }
        if (rule.getBizScope().isSpecificBiz()) {
            String bizStr = bizToControl(message, rule.getBizScope());
            if (bizStr != null) {
                Business biz = this.pusher.getBusinessFactory().getSubstanceByName(bizStr);
                if (rule.getReceiverScope().isAllReceiver()) {
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("saa attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("sae attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), null, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("sas attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
                if (rule.getReceiverScope().isEachReceiver()) {
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("sea attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("see attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), receiver, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ses attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
                if (rule.getReceiverScope().isSpecificReceiver()) {
                    String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                    if (specificReceiver != null) {
                        Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                        if (rule.getTunnelScope().isAllTunnel()) {
                            LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, biz, null);
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ssa attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                        if (rule.getTunnelScope().isEachTunnel()) {
                            LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, biz, tunnel);
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("sse attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                        if (rule.getTunnelScope().isSpecificTunnel()) {
                            String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                            if (specificTunnel != null) {
                                LocalDateTime time = pusher.getRecorder().lastAttemptTime(rule.getQuota(), specificReceiverObj, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                                if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                    LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                    return ValveTip.block(String.format("sss attempt %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                            suggestTime);
                                }
                            }
                        }
                    }
                }
            }
        }
        return ValveTip.ok();
    }

    private ValveTip controlError(Receiver receiver, Message message, AbstractTunnel tunnel, TimeWindowRule rule) {
        if (rule.getBizScope().isAllBiz()) {
            if (rule.getReceiverScope().isAllReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, null, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aaa error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, null, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aae error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("aas error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isEachReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, null, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aea error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, null, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("aee error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("aes error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isSpecificReceiver()) {
                String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                if (specificReceiver != null) {
                    Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, null, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("asa error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, null, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ase error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, null, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ass error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
            }
        }
        if (rule.getBizScope().isEachBiz()) {
            Business biz = message.getBiz();
            if (rule.getReceiverScope().isAllReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, biz, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eaa error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, biz, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eae error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("eas error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isEachReceiver()) {
                if (rule.getTunnelScope().isAllTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, biz, null);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eea error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isEachTunnel()) {
                    LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, biz, tunnel);
                    if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                        LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                        return ValveTip.block(String.format("eee error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                suggestTime);
                    }
                }
                if (rule.getTunnelScope().isSpecificTunnel()) {
                    String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                    if (specificTunnel != null) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ees error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                }
            }
            if (rule.getReceiverScope().isSpecificReceiver()) {
                String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                if (specificReceiver != null) {
                    Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("esa error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("ese error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ess error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
            }
        }
        if (rule.getBizScope().isSpecificBiz()) {
            String bizStr = bizToControl(message, rule.getBizScope());
            if (bizStr != null) {
                Business biz = this.pusher.getBusinessFactory().getSubstanceByName(bizStr);
                if (rule.getReceiverScope().isAllReceiver()) {
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("saa error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("sae error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), null, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("sas error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
                if (rule.getReceiverScope().isEachReceiver()) {
                    if (rule.getTunnelScope().isAllTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, biz, null);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("sea error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isEachTunnel()) {
                        LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, biz, tunnel);
                        if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                            LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                            return ValveTip.block(String.format("see error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                    suggestTime);
                        }
                    }
                    if (rule.getTunnelScope().isSpecificTunnel()) {
                        String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                        if (specificTunnel != null) {
                            LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), receiver, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ses error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                    }
                }
                if (rule.getReceiverScope().isSpecificReceiver()) {
                    String specificReceiver = receiverToControl(receiver.getName(), rule.getReceiverScope());
                    if (specificReceiver != null) {
                        Receiver specificReceiverObj = this.pusher.getReceiverFactory().getSubstanceByName(specificReceiver);
                        if (rule.getTunnelScope().isAllTunnel()) {
                            LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, biz, null);
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("ssa error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                        if (rule.getTunnelScope().isEachTunnel()) {
                            LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, biz, tunnel);
                            if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                return ValveTip.block(String.format("sse error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                        suggestTime);
                            }
                        }
                        if (rule.getTunnelScope().isSpecificTunnel()) {
                            String specificTunnel = tunnelToControl(tunnel.getName(), rule.getTunnelScope());
                            if (specificTunnel != null) {
                                LocalDateTime time = pusher.getRecorder().lastErrorTime(rule.getQuota(), specificReceiverObj, biz, this.pusher.getTunnelFactory().getSubstanceByName(specificTunnel));
                                if (time != null && Duration.between(time, LocalDateTime.now()).toMillis() <= rule.getMills()) {
                                    LocalDateTime suggestTime = time.plus(rule.getMills(), ChronoUnit.MILLIS);
                                    return ValveTip.block(String.format("sss error %d push from %s to %s", rule.getQuota(), time, suggestTime),
                                            suggestTime);
                                }
                            }
                        }
                    }
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
