package org.sanhenanli.togo.network.tunnel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/15 17:06
 * 推送结果枚举
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public enum TunnelTipCodeEnum {

    /**
     * 未知
     */
    UNKNOWN(1),
    /**
     * 成功
     */
    OK(2),
    /**
     * 失败
     */
    ERROR(3),
    ;

    private int code;
}
