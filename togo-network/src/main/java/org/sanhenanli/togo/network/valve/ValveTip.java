package org.sanhenanli.togo.network.valve;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/15 20:00
 * 推送控制结果
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ValveTip {

    /**
     * 是否阻止此次推送, true是
     */
    private final boolean block;
    /**
     * 阻止推送的原因描述
     */
    private final String tip;
    /**
     * 阻止推送后给出的建议推送时间
     */
    private final LocalDateTime suggestTime;

    public static ValveTip ok() {
        return new ValveTip(false, null, null);
    }

    public static ValveTip block(String tip, LocalDateTime suggestTime) {
        return new ValveTip(true, tip, suggestTime);
    }

    public boolean isOk() {
        return !block;
    }
}
