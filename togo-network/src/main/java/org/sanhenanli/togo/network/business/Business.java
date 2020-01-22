package org.sanhenanli.togo.network.business;

import lombok.Getter;

/**
 * datetime 2020/1/21 14:38
 *
 * @author zhouwenxiang
 */
@Getter
public class Business {

    protected String name;

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
