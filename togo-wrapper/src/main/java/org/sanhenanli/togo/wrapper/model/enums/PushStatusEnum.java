package org.sanhenanli.togo.wrapper.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
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

    public static PushStatusEnum getByStatus(int status) {
        for (PushStatusEnum value : PushStatusEnum.values()) {
            if (value.status == status) {
                return value;
            }
        }
        return null;
    }

    public static Set<PushStatusEnum> unfinished() {
        Set<PushStatusEnum> result = new HashSet<>(4);
        result.add(CREATED);
        result.add(PUSHING);
        return result;
    }

    public static Set<PushStatusEnum> succeed() {
        return Collections.singleton(SUCCESS);
    }

    public static Set<PushStatusEnum> failed() {
        return Collections.singleton(FAILED);
    }

    public static Set<PushStatusEnum> finished() {
        Set<PushStatusEnum> result = new HashSet<>(4);
        result.add(SUCCESS);
        result.add(FAILED);
        return result;
    }
}
