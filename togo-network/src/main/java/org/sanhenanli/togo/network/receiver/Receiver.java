package org.sanhenanli.togo.network.receiver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sanhenanli.togo.network.factory.Group;

/**
 * datetime 2020/1/22 9:27
 * 消息接收者
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor
@Getter
public class Receiver extends Group {

    /**
     * 接收者唯一标识, 也是通道使用的接收者标识
     */
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
