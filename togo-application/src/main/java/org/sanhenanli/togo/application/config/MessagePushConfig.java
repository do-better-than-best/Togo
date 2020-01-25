package org.sanhenanli.togo.application.config;

import org.sanhenanli.togo.network.business.Business;
import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.message.Message;
import org.sanhenanli.togo.network.policy.PushTunnelPolicy;
import org.sanhenanli.togo.network.policy.RetryPolicy;
import org.sanhenanli.togo.network.policy.RetryablePushPolicy;
import org.sanhenanli.togo.network.pusher.Pusher;
import org.sanhenanli.togo.network.trigger.InstantlyTrigger;
import org.sanhenanli.togo.wrapper.lock.PushLocker;
import org.sanhenanli.togo.wrapper.pusher.StandardPusherBuilder;
import org.sanhenanli.togo.wrapper.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * datetime 2020/1/25 18:47
 *
 * @author zhouwenxiang
 */
@Configuration
public class MessagePushConfig {

    @Autowired
    protected MessagePushRepository messagePushRepository;
    @Autowired
    protected MessageRepository messageRepository;
    @Autowired
    protected Executor pushExecutor;
    @Autowired
    protected PushLocker pushLocker;
    @Autowired
    protected ReceiverRepository receiverRepository;
    @Autowired
    protected BusinessRepository businessRepository;
    @Autowired
    protected TunnelRepository tunnelRepository;

    @Bean
    public Pusher messagePusher() {
        return new StandardPusherBuilder(
                messageRepository,
                messagePushRepository,
                pushLocker,
                pushExecutor,
                receiverRepository,
                businessRepository,
                tunnelRepository
        ).build();
    }

    public void test() {
        messagePusher().add("ephe",
                new Message("biz1id1", new Business("biz1"), "daniansanshi",
                        new RetryablePushPolicy(PushTunnelPolicy.ordered(30 * 1000), new InstantlyTrigger(),
                                new RetryPolicy(PushTunnelPolicy.INSTANTLY, new InstantlyTrigger(), 3, true))),
                "绿信通",
                false);
    }

}
