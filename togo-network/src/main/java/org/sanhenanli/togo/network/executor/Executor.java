package org.sanhenanli.togo.network.executor;

import java.time.LocalDateTime;

/**
 * datetime 2020/1/16 16:12
 * 执行器
 *
 * @author zhouwenxiang
 */
public interface Executor {

    /**
     * 添加任务到线程池
     * @param r runnable
     */
    void execute(Runnable r);

    /**
     * 添加任务到线程池
     * @param r runnable
     * @param first true优先执行
     */
    void execute(Runnable r, boolean first);

    /**
     * 添加定时任务
     * @param r runnable
     * @param time 触发时间
     */
    void executeOnSchedule(Runnable r, LocalDateTime time);
}
