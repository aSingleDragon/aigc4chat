package pers.hll.aigc4chat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 线程池配置
 *
 * @author hll
 * @since 2024/03/31
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(ThreadPoolName.LOGIN)
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("login-task-");
        taskScheduler.setPoolSize(4);
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean(ThreadPoolName.ASYNC_LOGIN)
    public SimpleAsyncTaskExecutor asyncExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setThreadNamePrefix("async-login-");
        executor.setConcurrencyLimit(1);
        return executor;
    }
}