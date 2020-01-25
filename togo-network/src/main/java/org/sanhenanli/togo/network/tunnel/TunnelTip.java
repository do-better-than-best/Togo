package org.sanhenanli.togo.network.tunnel;

import lombok.AccessLevel;
import org.sanhenanli.togo.network.valve.ValveTip;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCauseEnum;
import org.sanhenanli.togo.network.tunnel.enums.TunnelTipCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/15 17:04
 * 推送结果
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class TunnelTip {

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

    public static TunnelTip ok() {
        return new TunnelTip(TunnelTipCodeEnum.OK, null, null, null);
    }

    public static TunnelTip error(String tip) {
        return new TunnelTip(TunnelTipCodeEnum.ERROR, null, tip, null);
    }

    public static TunnelTip notConnected() {
        return new TunnelTip(TunnelTipCodeEnum.ERROR, TunnelTipCauseEnum.NOT_CONNECTED, null, null);
    }

    public static TunnelTip noReceipt() {
        return new TunnelTip(TunnelTipCodeEnum.ERROR, TunnelTipCauseEnum.NO_RECEIPT, null, null);
    }

    public static TunnelTip blocked(ValveTip valveTip) {
        return new TunnelTip(TunnelTipCodeEnum.ERROR, TunnelTipCauseEnum.BLOCKED, valveTip.getTip(), valveTip.getSuggestTime());
    }

    public boolean isOk() {
        return code == TunnelTipCodeEnum.OK;
    }

    public boolean isError() {
        return code == TunnelTipCodeEnum.ERROR;
    }

    public boolean isNotConnected() {
        return cause == TunnelTipCauseEnum.NOT_CONNECTED;
    }

    public boolean isBlocked() {
        return cause == TunnelTipCauseEnum.BLOCKED;
    }

    public boolean isNoReceipt() {
        return cause == TunnelTipCauseEnum.NO_RECEIPT;
    }


}
