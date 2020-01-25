package org.sanhenanli.togo.network.business;

import lombok.Getter;
import org.sanhenanli.togo.network.factory.Group;
import org.sanhenanli.togo.network.factory.enums.GroupLevelEnum;

/**
 * datetime 2020/1/21 14:38
 * 消息所属业务
 *
 * @author zhouwenxiang
 */
@Getter
public class Business extends Group {

    /**
     * 消息所属业务名, 是业务的唯一标识
     */
    protected String name;

    public Business(String name, GroupLevelEnum level) {
        super(level);
        this.name = name;
    }

    public Business(String name) {
        this.name = name;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }
        Business o = (Business) obj;
        return this.name != null && this.name.equals(o.name);
    }

    @Override
    public final int hashCode() {
        return this.name.hashCode();
    }
}
