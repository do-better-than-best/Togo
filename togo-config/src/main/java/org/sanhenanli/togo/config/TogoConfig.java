package org.sanhenanli.togo.config;

import org.sanhenanli.togo.application.executor.SimpleExecutor;
import org.sanhenanli.togo.application.lock.InMemoryPushLocker;
import org.sanhenanli.togo.application.repository.InMemoryTunnelRepository;
import org.sanhenanli.togo.application.repository.SimpleBusinessRepository;
import org.sanhenanli.togo.application.repository.SimpleReceiverRepository;
import org.sanhenanli.togo.config.tunnel.ConsoleTunnel;
import org.sanhenanli.togo.config.tunnel.LogTunnel;
import org.sanhenanli.togo.network.executor.Executor;
import org.sanhenanli.togo.network.pusher.StandardPusher;
import org.sanhenanli.togo.api.lock.PushLocker;
import org.sanhenanli.togo.api.pusher.StandardPusherBuilder;
import org.sanhenanli.togo.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * datetime 2020/1/25 18:47
 * 推送器配置
 *
 * @author zhouwenxiang
 */
@Configuration
public class TogoConfig {

    @Autowired
    protected MessagePushRepository messagePushRepository;
    @Autowired
    protected MessageRepository messageRepository;

    @Bean
    public PushLocker pushLocker() {
        return new InMemoryPushLocker();
    }

    @Bean
    public TunnelRepository tunnelRepository() {
        return new InMemoryTunnelRepository();
    }

    @Bean
    public ReceiverRepository receiverRepository() {
        return new SimpleReceiverRepository();
    }

    @Bean
    public BusinessRepository businessRepository() {
        return new SimpleBusinessRepository();
    }

    @Bean
    public Executor pushExecutor() {
        return new SimpleExecutor();
    }

    /**
     * 配置标准推送器
     * @return 标准推送器
     */
    @Bean
    public StandardPusher togoPusher() {
        return new StandardPusherBuilder(
                messageRepository,
                messagePushRepository,
                pushLocker(),
                pushExecutor(),
                receiverRepository(),
                businessRepository(),
                tunnelRepository()
        ).build();
    }

    /**
     * 控制台通道 name
     */
    public static final String TUNNEL_CONSOLE_NAME = "console";
    /**
     * 日志通道 name
     */
    public static final String TUNNEL_LOG_NAME = "log";

    @PostConstruct
    public void init() {
        StandardPusher pusher = togoPusher();
        // 注册 控制台tunnel
        pusher.getTunnelFactory().register(new ConsoleTunnel(TUNNEL_CONSOLE_NAME, pusher), null);
        pusher.getTunnelFactory().register(new LogTunnel(TUNNEL_LOG_NAME, pusher), null);
        // 开始消息推送(历史遗留消息)
        togoPusher().onStart();
    }

}
