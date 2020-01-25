package org.sanhenanli.togo.network.rule;

import lombok.Getter;
import org.sanhenanli.togo.network.tunnel.AbstractTunnel;

import java.util.List;

/**
 * datetime 2020/1/22 11:26
 * 对通道的规则描述
 *
 * @author zhouwenxiang
 */
@Getter
public class RuleScopeOfTunnel implements RuleScope {

    public static final RuleScopeOfTunnel DEFAULT = new RuleScopeOfTunnel(false, true, false, null);

    protected boolean allTunnel;
    protected boolean eachTunnel;
    protected boolean specificTunnel;
    protected List<AbstractTunnel> specificTunnels;

    public RuleScopeOfTunnel(boolean allTunnel, boolean eachTunnel, boolean specificTunnel, List<AbstractTunnel> specificTunnels) {
        assert allTunnel || eachTunnel || specificTunnel;
        assert !specificTunnel || specificTunnels != null && !specificTunnels.isEmpty();
        this.allTunnel = allTunnel;
        this.eachTunnel = eachTunnel;
        this.specificTunnel = specificTunnel;
        this.specificTunnels = specificTunnels;
    }

}
