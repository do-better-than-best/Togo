package org.sanhenanli.togo.network.tunnel;

import lombok.Getter;
import org.sanhenanli.togo.network.valve.ValveTip;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCodeEnum;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * datetime 2020/1/15 17:04
 * 推送结果
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class TunnelTip implements Serializable {

    private static final long serialVersionUID = -9221134829043745267L;
    /**
     * 通道名称
     */
    private String tunnel;
    /**
     * 推送结果码
     */
    private TunnelTipCodeEnum code;
    /**
     * 推送失败的原因
     */
    private TunnelTipCauseEnum cause;
    /**
     * 推送失败的描述
     */
    private String tip;
    /**
     * 推送失败后的建议重试时间
     */
    private LocalDateTime suggestTime;

    public static TunnelTip ok(String tunnel) {
        return new TunnelTip(tunnel, TunnelTipCodeEnum.OK, null, null, null);
    }

    public static TunnelTip ok(String tunnel, String tip) {
        return new TunnelTip(tunnel, TunnelTipCodeEnum.OK, null, tip, null);
    }

    public static TunnelTip error(String tunnel, String tip) {
        return new TunnelTip(tunnel, TunnelTipCodeEnum.ERROR, null, tip, null);
    }

    public static TunnelTip notConnected(String tunnel) {
        return new TunnelTip(tunnel, TunnelTipCodeEnum.ERROR, TunnelTipCauseEnum.NOT_CONNECTED, null, null);
    }

    public static TunnelTip noReceipt(String tunnel) {
        return new TunnelTip(tunnel, TunnelTipCodeEnum.ERROR, TunnelTipCauseEnum.NO_RECEIPT, null, null);
    }

    public static TunnelTip blocked(String tunnel, ValveTip valveTip) {
        return new TunnelTip(tunnel, TunnelTipCodeEnum.ERROR, TunnelTipCauseEnum.BLOCKED, valveTip.getTip(), valveTip.getSuggestTime());
    }

    public boolean isOk() {
        return code == TunnelTipCodeEnum.OK;
    }

    public boolean isError() {
        return code == TunnelTipCodeEnum.ERROR;
    }

    public boolean causeNotConnected() {
        return cause == TunnelTipCauseEnum.NOT_CONNECTED;
    }

    public boolean causeBlocked() {
        return cause == TunnelTipCauseEnum.BLOCKED;
    }

    public boolean causeNoReceipt() {
        return cause == TunnelTipCauseEnum.NO_RECEIPT;
    }

}
