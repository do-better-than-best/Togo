package org.sanhenanli.togo.network.receiver;

import lombok.Getter;

/**
 * datetime 2020/1/22 9:27
 *
 * @author zhouwenxiang
 */
@Getter
public class Receiver {

    protected String name;

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }
        Receiver o = (Receiver) obj;
        return this.name != null && this.name.equals(o.name);
    }

    @Override
    public final int hashCode() {
        return this.name.hashCode();
    }
}
