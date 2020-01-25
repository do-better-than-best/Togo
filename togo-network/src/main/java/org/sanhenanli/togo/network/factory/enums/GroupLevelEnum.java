package org.sanhenanli.togo.network.factory.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * datetime 2020/1/24 16:30
 * 组管理中的对象级别
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public enum GroupLevelEnum {

    /**
     * 实体
     */
    SUBSTANCE,
    /**
     * 虚拟, 是多个实体或虚拟的集合
     */
    VIRTUAL,
    ;
}
