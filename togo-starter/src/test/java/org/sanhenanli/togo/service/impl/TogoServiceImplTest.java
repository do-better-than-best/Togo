package org.sanhenanli.togo.service.impl;

import org.junit.Test;
import org.sanhenanli.togo.TogoApplicationTest;
import org.sanhenanli.togo.config.TogoConfig;
import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.policy.PushTunnelPolicy;
import org.sanhenanli.togo.network.policy.RetryPolicy;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.network.pusher.StandardPusher;
import org.sanhenanli.togo.network.rule.RuleScopeOfReceiver;
import org.sanhenanli.togo.network.rule.RuleScopeOfTunnel;
import org.sanhenanli.togo.network.rule.TimeGapRule;
import org.sanhenanli.togo.network.trigger.InstantlyTrigger;
import org.sanhenanli.togo.network.trigger.ScheduleTrigger;
import org.sanhenanli.togo.network.valve.TimeGapValve;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * datetime 2020/9/27 10:18
 *
 * @author zhouwenxiang
 */
public class TogoServiceImplTest extends TogoApplicationTest {

    @Autowired
    private StandardPusher togoPusher;

    @Test
    public void onStart() throws InterruptedException {
        Thread.sleep(10000);
    }

    @Test
    public void generalPush() throws InterruptedException {
        togoPusher.add(
                "r1",
                new Message(
                        "1",
                        new Business("b1"),
                        "this is my general msg",
                        new RetryablePushPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), RetryPolicy.NO_RETRY)
                ),
                TogoConfig.TUNNEL_CONSOLE_NAME,
                false);
        Thread.sleep(10000);
    }

    @Test
    public void scheduledPush() throws InterruptedException {
        togoPusher.add(
                "r2",
                new Message(
                        "2",
                        new Business("b1"),
                        "this is my scheduled msg",
                        new RetryablePushPolicy(PushTunnelPolicy.INSTANTLY, ScheduleTrigger.at(LocalDateTime.now().plusSeconds(5)), RetryPolicy.NO_RETRY)
                ),
                TogoConfig.TUNNEL_LOG_NAME,
                false
        );
        Thread.sleep(10000);
    }

    @Test
    public void valveTest() throws InterruptedException {
        TimeGapValve tmpValue = new TimeGapValve(new TimeGapRule(3).receiverScope(RuleScopeOfReceiver.DEFAULT).tunnelScope(RuleScopeOfTunnel.DEFAULT));
        togoPusher.addValve(tmpValue);
        for (int i = 0; i < 5; i++) {
            togoPusher.add(
                    "r1",
                    new Message(
                            String.valueOf(10 + i),
                            new Business("b1"),
                            "this is msg NO." + i,
                            new RetryablePushPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), RetryPolicy.NO_RETRY)
                    ),
                    TogoConfig.TUNNEL_CONSOLE_NAME,
                    false);
        }
        Thread.sleep(10000);
        togoPusher.removeValve(tmpValue);
    }

    @Test
    public void suggestTimeTest() throws InterruptedException {
        TimeGapValve tmpValue = new TimeGapValve(new TimeGapRule(2).receiverScope(RuleScopeOfReceiver.DEFAULT).tunnelScope(RuleScopeOfTunnel.DEFAULT));
        togoPusher.addValve(tmpValue);
        for (int i = 0; i < 5; i++) {
            togoPusher.add(
                    "r3",
                    new Message(
                            String.valueOf(20 + i),
                            new Business("b2"),
                            "this is msg NO." + (20 + i),
                            new RetryablePushPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), new RetryPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), 1, true))
                    ),
                    TogoConfig.TUNNEL_LOG_NAME,
                    false);
        }
        Thread.sleep(15000);
        togoPusher.removeValve(tmpValue);
    }

    @Test
    public void orderedPush() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            togoPusher.add(
                    "r3",
                    new Message(
                            String.valueOf(50 + i),
                            new Business("b2"),
                            "this is msg NO." + (50 + i),
                            new RetryablePushPolicy(PushTunnelPolicy.ordered(10000), new InstantlyTrigger(), RetryPolicy.NO_RETRY)
                    ),
                    TogoConfig.TUNNEL_LOG_NAME,
                    false);
        }
        Thread.sleep(30000);
    }

    /**
     * 最简场景下, 1s能推送约4条消息; 240/m, 14400/h, 345600/d
     */
    @Test
    public void pressureTest() throws InterruptedException {
        togoPusher.getTunnelFactory().register(togoPusher.getTunnelFactory().getSubstanceByName(TogoConfig.TUNNEL_LOG_NAME), "tt1");
        togoPusher.getTunnelFactory().register(togoPusher.getTunnelFactory().getSubstanceByName(TogoConfig.TUNNEL_CONSOLE_NAME), "tt1");

        for (int i = 0; i < 1000; i++) {
            togoPusher.add(
                    "r1",
                    new Message(
                            String.valueOf(1000 + i),
                            new Business("b3"),
                            "this is msg NO." + (1000 + i),
                            new RetryablePushPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), RetryPolicy.NO_RETRY)
                    ),
                    "tt1",
                    false);
        }
        Thread.sleep(1000 * 60 * 10);
    }
}