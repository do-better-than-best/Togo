package org.sanhenanli.togo.network.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sanhenanli.togo.network.business.Business;

/**
 * datetime 2020/1/26 13:29
 * 定义name属性对象
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class Name {

    /**
     * 对象name, 是对象的唯一标识
     */
    protected String name;

    @Override
    public boolean equals(Object obj) {
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
    public int hashCode() {
        return this.name.hashCode();
    }
}
