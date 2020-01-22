package org.sanhenanli.togo.network.rule;

import org.sanhenanli.togo.network.tunnel.AbstractTunnel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * datetime 2020/1/22 11:26
 *
 * @author zhouwenxiang
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class RuleScopeOfTunnel implements RuleScope {

    public static final RuleScopeOfTunnel DEFAULT = eachTunnel();

    protected boolean allTunnel;
    protected boolean eachTunnel;
    protected boolean specificTunnel;
    protected List<AbstractTunnel> specificTunnels;

    public static RuleScopeOfTunnel eachTunnel() {
        return new RuleScopeOfTunnel(false, true, false, null);
    }
}
