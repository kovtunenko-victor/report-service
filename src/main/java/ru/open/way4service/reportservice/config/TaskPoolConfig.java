package ru.open.way4service.reportservice.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class TaskPoolConfig {
    @Value("${taskExecutor.corePoolSize}")
    private int corePoolSize;
    @Value("${taskExecutor.maxPoolSize}")
    private int maxPoolSize;
    @Value("${taskExecutor.queueCapacity}")
    private int queueCapacity;
    
    @Bean("taskExecutor") 
    public Executor getTaskExecutor() { 
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize (corePoolSize); 
        executor.setMaxPoolSize (maxPoolSize);
        executor.setQueueCapacity (queueCapacity); 
        executor.setKeepAliveSeconds (60); 
        executor.setThreadNamePrefix ("taskExecutor -");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize (); 
        
        return executor;
    }
}
