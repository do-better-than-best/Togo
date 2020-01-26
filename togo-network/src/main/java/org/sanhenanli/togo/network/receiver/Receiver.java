package org.sanhenanli.togo.network.receiver;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sanhenanli.togo.network.factory.Name;

/**
 * datetime 2020/1/22 9:27
 * 消息接收者
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class Receiver extends Name {

    public Receiver(String name) {
        super(name);
    }
}
