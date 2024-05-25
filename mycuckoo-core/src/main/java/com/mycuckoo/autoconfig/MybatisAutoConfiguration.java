package com.mycuckoo.autoconfig;

import com.mycuckoo.core.repository.PageInterceptor;
import com.mycuckoo.core.web.filter.RequestLoggingFilter;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 8:57
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Interceptor.class)
public class MybatisAutoConfiguration {

    @Bean
    public Interceptor interceptors() {
        return new PageInterceptor();
    }
}
