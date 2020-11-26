package org.sanhenanli.togo.application.executor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sanhenanli.togo.network.executor.Executor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * datetime 2020/1/18 20:17
 *
 * @author zhouwenxiang
 */
@Slf4j
public class SimpleExecutor implements Executor {

    private ThreadPoolTaskExecutor executorService;
    private ThreadPoolTaskExecutor highPriorityExecutorService;
    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void init() {
        executorService = new ThreadPoolTaskExecutor();
        executorService.setCorePoolSize(30);
        executorService.setMaxPoolSize(100);
        executorService.setKeepAliveSeconds(60);
        executorService.setQueueCapacity(10000);
        executorService.setThreadNamePrefix("do-push-thread-");
        executorService.setRejectedExecutionHandler((r, executor) -> log.error("reject task"));
        executorService.initialize();

        highPriorityExecutorService = new ThreadPoolTaskExecutor();
        highPriorityExecutorService.setCorePoolSize(10);
        highPriorityExecutorService.setMaxPoolSize(30);
        highPriorityExecutorService.setKeepAliveSeconds(60);
        highPriorityExecutorService.setQueueCapacity(10000);
        highPriorityExecutorService.setThreadNamePrefix("start-push-thread-");
        highPriorityExecutorService.setRejectedExecutionHandler((r, executor) -> log.error("reject task"));
        executorService.initialize();

        scheduledExecutorService = new ScheduledThreadPoolExecutor(30,
                new CustomizableThreadFactory("scheduled-push-thread-"), (r, executor) -> log.error("reject task"));
    }

    @Override
    public void execute(Runnable r) {
        executorService.execute(r);
    }

    @Override
    public void execute(Runnable r, boolean first) {
        if (first) {
            highPriorityExecutorService.execute(r);
        } else {
            executorService.execute(r);
        }
    }

    @SneakyThrows
    @Override
    public void executeOnSchedule(Runnable r, LocalDateTime time) {
        long delay = Duration.between(LocalDateTime.now(), time).getSeconds();
        delay = delay < 0 ? 0 : delay;
        scheduledExecutorService.schedule(r, delay, TimeUnit.SECONDS);
    }
}
