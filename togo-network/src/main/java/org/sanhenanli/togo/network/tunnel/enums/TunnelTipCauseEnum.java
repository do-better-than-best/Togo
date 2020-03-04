package org.sanhenanli.togo.network.tunnel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/18 15:56
 * 推送失败原因枚举
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public enum TunnelTipCauseEnum {

    /**
     * 未知
     */
    UNKNOWN(1),
    /**
     * 被控制器限制
     */
    BLOCKED(2),
    /**
     * 接收者未建立通道连接
     */
    NOT_CONNECTED(3),
    /**
     * 没有收到消息回执
     */
    NO_RECEIPT(4),
    ;

    /**
     * 原因code
     */
    private int cause;

    public static TunnelTipCauseEnum getByCause(int cause) {
        for (TunnelTipCauseEnum value : TunnelTipCauseEnum.values()) {
            if (value.cause == cause) {
                return value;
            }
        }
        return null;
    }
}
