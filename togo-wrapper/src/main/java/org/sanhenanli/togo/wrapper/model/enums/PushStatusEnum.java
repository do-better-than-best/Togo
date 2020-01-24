package org.sanhenanli.togo.wrapper.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * datetime 2020/1/24 15:12
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public enum PushStatusEnum {

    CREATED(1),
    PUSHING(2),
    SUCCESS(3),
    FAILED(4),
    ;

    private int status;

    public static Set<PushStatusEnum> unfinished() {
        Set<PushStatusEnum> result = new HashSet<>(4);
        result.add(CREATED);
        result.add(PUSHING);
        return result;
    }
}
