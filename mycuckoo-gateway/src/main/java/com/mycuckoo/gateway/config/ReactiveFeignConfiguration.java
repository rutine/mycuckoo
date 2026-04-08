package com.mycuckoo.gateway.config;

import com.mycuckoo.autoconfig.FeignLoggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.client.log.ReactiveLoggerListener;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(FeignLoggerProperties.class)
public class ReactiveFeignConfiguration {

    @Bean("reactiveLogger")
    public ReactiveLoggerListener<MyReactiveLoggerListener.LogContext> reactiveLogger(FeignLoggerProperties properties) {
        return new MyReactiveLoggerListener(properties);
    }
}
