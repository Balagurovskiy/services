package com.microtask.msggenerator.security;


import com.microtask.msggenerator.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class AuthScheduleConfig implements SchedulingConfigurer {

    private final AuthService authService;

    @Value("${auth.schedule.delay}")
    private int asyncDelay;
    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                () -> {
                        log.info("Initiating auth task...");
                        authService.refreshToken();
                },
                (TriggerContext triggerContext) -> {
                        int expiry = authService.getExpiresSec() * 1000 - asyncDelay;
                        Optional<Date> lastCompletionTime =
                                Optional.ofNullable(triggerContext.lastCompletionTime());
                        Instant nextExecutionTime =
                                lastCompletionTime.orElseGet(Date::new).toInstant()
                                        .plusMillis(expiry);
                        log.info(String.format("Creating next auth schedule [%s | %s]", expiry, Date.from(nextExecutionTime)));
                        return nextExecutionTime;
                }
        );
    }
}

