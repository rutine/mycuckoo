package com.mycuckoo.autoconfig;

import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.web.filter.RequestLoggingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 8:57
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(OncePerRequestFilter.class)
public class WebAutoConfiguration {

    @Bean
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ApplicationEventMulticaster.class)
    static class OperatorConfiguration {
        OperatorConfiguration(ApplicationEventMulticaster multicaster) {
            LogOperator.setEventMulticaster(multicaster);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RestControllerAdvice.class)
    static class ExceptionHandlerConfiguration {

        @RestControllerAdvice
        public class MycuckooExceptionHandler extends com.mycuckoo.core.exception.MycuckooExceptionHandler {

        }
    }
}
