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
     * 成功
     */
    OK(1),
    /**
     * 失败
     */
    ERROR(2),
    ;

    /**
     * code
     */
    private final int code;
}
