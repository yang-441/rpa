package com.deepscience.rpa.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务配置
 * @author yangzhuo
 * @date 2025/1/26 17:08
 */
@Configuration
public class ScheduledConfig {
    /**
     * 定时任务线程池
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        // 创建并配置线程池调度器
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 设置线程池的大小
        scheduler.setPoolSize(5);
        // 设置线程名前缀
        scheduler.setThreadNamePrefix("scheduled-task-");
        return scheduler;
    }
}
