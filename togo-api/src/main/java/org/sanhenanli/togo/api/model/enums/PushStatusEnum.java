package org.sanhenanli.togo.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * datetime 2020/1/24 15:12
 * 推送状态枚举
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public enum PushStatusEnum {

    /**
     * 未推送
     */
    CREATED(1),
    /**
     * 推送中
     */
    PUSHING(2),
    /**
     * 推送成功
     */
    SUCCESS(3),
    /**
     * 推送失败
     */
    FAILED(4),
    /**
     * 推送失败并可重试
     */
    RETRYING(5)
    ;

    private final int status;

    public static PushStatusEnum getByStatus(int status) {
        for (PushStatusEnum value : PushStatusEnum.values()) {
            if (value.status == status) {
                return value;
            }
        }
        return null;
    }

    /**
     * 未完成状态set
     * @return set
     */
    public static Set<PushStatusEnum> unfinished() {
        Set<PushStatusEnum> result = new HashSet<>(4);
        result.add(CREATED);
        result.add(PUSHING);
        return result;
    }

    /**
     * 成功状态set
     * @return set
     */
    public static Set<PushStatusEnum> succeed() {
        return Collections.singleton(SUCCESS);
    }

    /**
     * 失败状态set
     * @return set
     */
    public static Set<PushStatusEnum> failed() {
        return Collections.singleton(FAILED);
    }

    /**
     * 完成状态set
     * @return set
     */
    public static Set<PushStatusEnum> finished() {
        Set<PushStatusEnum> result = new HashSet<>(4);
        result.add(SUCCESS);
        result.add(FAILED);
        result.add(RETRYING);
        return result;
    }
}
