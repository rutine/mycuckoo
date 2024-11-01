package com.mycuckoo.autoconfig;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author rutine
 * @date 2024/8/22 9:33
 */
@Configuration
@EnableConfigurationProperties(FeignLoggerProperties.class)
@ConditionalOnClass({ Feign.class, FeignLoggerFactory.class })
public class FeignAutoConfiguration implements RequestInterceptor {

    private static final String REQUEST_ID = "X-Request-Id";
    private static final String AUTHORIZATION = "Authorization";

    private static final ThreadLocal<HttpHeaders> httpHeadersHolder = new NamedThreadLocal<>("HttpHeaders");

    public static void setHttpHeaders(HttpHeaders headers) {
        if (headers != null) {
            httpHeadersHolder.set(headers);
        }
    }

    public static void clearHttpHeaders() {
        httpHeadersHolder.set(null);
    }


    @Override
    public void apply(RequestTemplate template) {
        //webmvc
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            if (attributes.getRequest().getHeader(AUTHORIZATION) != null) {
                template.header(AUTHORIZATION, request.getHeader(AUTHORIZATION));
            }
            if (attributes.getRequest().getHeader(REQUEST_ID) != null) {
                template.header(REQUEST_ID, request.getHeader(REQUEST_ID));
                return;
            }
        }

        //flux webmvc
        HttpHeaders headers = httpHeadersHolder.get();
        if (headers != null) {
            if (headers.containsKey(AUTHORIZATION)) {
                template.header(AUTHORIZATION, headers.getFirst(AUTHORIZATION));
            }
            if (headers.containsKey(REQUEST_ID)) {
                template.header(REQUEST_ID, headers.getFirst(REQUEST_ID));
                return;
            }
        }

        if (MDC.get("traceId") != null) {
            template.header(REQUEST_ID, MDC.get("traceId"));
        }
    }


    @Bean
    public FeignLoggerFactory feignLoggerFactory(FeignLoggerProperties loggerProperties) {
        return (clazz) -> new MySlf4jLogger(clazz, loggerProperties);
    }

}
