package org.sanhenanli.togo.application.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sanhenanli.togo.application.tunnel.TestStatelessTunnel;
import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.policy.PushTunnelPolicy;
import org.sanhenanli.togo.network.policy.RetryPolicy;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.network.pusher.StandardPusher;
import org.sanhenanli.togo.network.rule.RuleScopeOfPush;
import org.sanhenanli.togo.network.rule.RuleScopeOfReceiver;
import org.sanhenanli.togo.network.rule.RuleScopeOfTunnel;
import org.sanhenanli.togo.network.rule.TimeWindowRule;
import org.sanhenanli.togo.network.trigger.InstantlyTrigger;
import org.sanhenanli.togo.network.valve.TimeWindowValve;
import org.sanhenanli.togo.wrapper.repository.TunnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * datetime 2020/1/25 21:49
 *
 * @author zhouwenxiang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MessagePushConfigTest {

    @Autowired
    private StandardPusher messagePusher;
    @Autowired
    private TunnelRepository tunnelRepository;

    @Before
    public void before() {
        // 添加一个通道
        TestStatelessTunnel tunnel = new TestStatelessTunnel("test-stateless", messagePusher);
        tunnelRepository.register(tunnel, "test");

        // 添加控制阀门
        messagePusher
                .addValve(new TimeWindowValve(new TimeWindowRule()
                        .receiverScope(new RuleScopeOfReceiver(false, true, true,
                                Collections.singletonList("ephe"))).pushScope(new RuleScopeOfPush(true, false, true))
                        .tunnelScope(new RuleScopeOfTunnel(false, false, true, Collections.singletonList("test")))));
    }

    @Test
    public void init() {
        messagePusher.onStart();
    }

    @Test
    public void add() {
        messagePusher.add("ephe",
                new Message("biz1id1", new Business("biz1"), "daniansanshi",
                        new RetryablePushPolicy(PushTunnelPolicy.ordered(30 * 1000), new InstantlyTrigger(),
                                new RetryPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), 3, true))),
                "绿信通",
                false);
    }

}