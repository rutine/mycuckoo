package com.mycuckoo.gateway.config;

import com.mycuckoo.gateway.client.UserClient;
import com.mycuckoo.gateway.filter.GatewayPrivilegeFilter;
import com.mycuckoo.gateway.filter.LoggingFilter;
import com.mycuckoo.gateway.filter.TracerFilter;
import com.mycuckoo.gateway.handle.predicate.ReadBodyRoutePredicateFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.function.Predicate;
import java.util.stream.Collectors;


@Configuration
@EnableConfigurationProperties(PrivilegeProperties.class)
public class ContextConfig {

    @Bean
    public GlobalFilter tracerFilter() {
        return new TracerFilter();
    }

    @Bean
    public GlobalFilter loggingFilter() {return new LoggingFilter(); }

    @Bean
    public GlobalFilter gatewayPrivilegeFilter(PrivilegeProperties properties, UserClient client) {
        return new GatewayPrivilegeFilter(properties, client);
    }

    @Bean
    public Predicate readBodyPredicate() {
        return (body) -> true;
    }

    @Bean
    public ReadBodyRoutePredicateFactory readBodyRoutePredicateFactory() {
        return new ReadBodyRoutePredicateFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
