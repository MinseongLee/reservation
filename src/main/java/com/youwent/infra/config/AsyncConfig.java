package com.youwent.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig implements AsyncConfigurer {
    // default는 새로운 thread 하나 생성
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processorCnt= {}", processors);
        // 현재 일하고 있는 스레드의 개수 (최적상태)
        executor.setCorePoolSize(processors);
        // Queue에 쓰레드 50까지만 대기 시킴.
        executor.setQueueCapacity(50);
        // 만약 쓰레드 51까지 오면 새로운 pool 생성. maxPoolSize까지만 이 사이클로 처리.
        // 이 맥스를 넘기면 task를 처리 못함.
        executor.setMaxPoolSize(processors * 2);
        // 최적 상태가 넘어간 pool을 60초가 넘어가면 정리.
        executor.setKeepAliveSeconds(60);
        // for log and error
        executor.setThreadNamePrefix("AsyncExecutor=");
        executor.initialize();
        return AsyncConfigurer.super.getAsyncExecutor();
    }
}
