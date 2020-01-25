package org.sanhenanli.togo.network.factory;

import lombok.Getter;
import org.sanhenanli.togo.network.factory.enums.GroupLevelEnum;

/**
 * datetime 2020/1/24 16:22
 * 组管理基础对象 todo 优化组管理的level
 *
 * @author zhouwenxiang
 */
@Getter
public class Group {

    public Group() {
    }

    public Group(GroupLevelEnum level) {
        this.level = level;
    }

    /**
     * 对象级别
     */
    protected GroupLevelEnum level = GroupLevelEnum.SUBSTANCE;
}
