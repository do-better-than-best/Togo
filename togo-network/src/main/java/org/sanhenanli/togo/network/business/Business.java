package org.sanhenanli.togo.network.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sanhenanli.togo.network.factory.Group;

/**
 * datetime 2020/1/21 14:38
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class Business extends Group {

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
