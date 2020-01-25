package org.sanhenanli.togo.network.trigger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/16 9:59
 * 定时推送触发器
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ScheduleTrigger implements PushTrigger {

    /**
     * 定时
     */
    private LocalDateTime schedule;

    public static ScheduleTrigger at(LocalDateTime time) {
        return new ScheduleTrigger(time);
    }
}
