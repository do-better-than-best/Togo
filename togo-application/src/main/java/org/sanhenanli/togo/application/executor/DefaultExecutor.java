package org.sanhenanli.togo.application.executor;

import org.sanhenanli.togo.network.executor.Executor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * datetime 2020/1/18 20:17
 *
 * @author zhouwenxiang
 */
@Component
@ConditionalOnMissingBean(Executor.class)
public class DefaultExecutor implements Executor {

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        // todo block when full
        executorService = new ThreadPoolExecutor(
                1, 1, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(10));
    }

    @Override
    public void execute(Runnable r) {
        executorService.execute(r);
    }

    @Override
    public void execute(Runnable r, boolean first) {
        // todo 加到线程池头部
    }

    @Override
    public void executeOnSchedule(Runnable r, LocalDateTime time) {
        // todo 创建定时任务
    }
}
