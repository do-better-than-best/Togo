package org.sanhenanli.togo.application.executor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sanhenanli.togo.network.executor.Executor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

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

    private ExecutorService executorService;
    private ExecutorService highPriorityExecutorService;
    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void init() {
        executorService = new ThreadPoolExecutor(
                30, 100, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(10000),
                new CustomizableThreadFactory("do-push-thread-"), (r, executor) -> log.error("reject task"));

        highPriorityExecutorService = new ThreadPoolExecutor(
                10, 30, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(10000),
                new CustomizableThreadFactory("start-push-thread-"), (r, executor) -> log.error("reject task"));

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
