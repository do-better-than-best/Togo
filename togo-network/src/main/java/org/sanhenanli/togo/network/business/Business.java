package org.sanhenanli.togo.network.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.sanhenanli.togo.network.factory.Name;

import java.io.Serializable;

/**
 * datetime 2020/1/21 14:38
 * 消息所属业务
 *
 * @author zhouwenxiang
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class Business extends Name implements Serializable {

    private static final long serialVersionUID = -8647067574240332331L;

    public Business(String name) {
        super(name);
    }
}
