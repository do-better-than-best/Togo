package org.sanhenanli.togo.application.config;

import org.sanhenanli.togo.application.lock.InMemoryPushLock;
import org.sanhenanli.togo.application.repository.InMemoryTunnelRepository;
import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.pusher.StandardPusher;
import org.sanhenanli.togo.wrapper.lock.PushLocker;
import org.sanhenanli.togo.wrapper.pusher.StandardPusherBuilder;
import org.sanhenanli.togo.wrapper.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * datetime 2020/1/25 18:47
 * 推送器配置
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
    protected ReceiverRepository receiverRepository;
    @Autowired
    protected BusinessRepository businessRepository;

    @Bean
    public PushLocker pushLocker() {
        return new InMemoryPushLock();
    }

    @Bean
    public TunnelRepository tunnelRepository() {
        return new InMemoryTunnelRepository();
    }

    /**
     * 配置标准推送器
     * @return 标准推送器
     */
    @Bean
    public StandardPusher messagePusher() {
        return new StandardPusherBuilder(
                messageRepository,
                messagePushRepository,
                pushLocker(),
                pushExecutor,
                receiverRepository,
                businessRepository,
                tunnelRepository()
        ).build();
    }

}
