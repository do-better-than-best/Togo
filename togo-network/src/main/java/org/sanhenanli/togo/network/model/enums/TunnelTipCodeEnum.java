package org.sanhenanli.togo.network.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/15 17:06
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public enum TunnelTipCodeEnum {

    UNKNOWN(1),
    OK(2),
    ERROR(3),
    ;

    private int code;
}
