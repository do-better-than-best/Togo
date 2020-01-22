package org.sanhenanli.togo.network.tunnel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/18 15:56
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public enum TunnelTipCauseEnum {

    UNKNOWN(1),
    BLOCKED(2),
    NOT_CONNECTED(3),
    NO_RECEIPT(4),
    ;

    private int cause;
}
