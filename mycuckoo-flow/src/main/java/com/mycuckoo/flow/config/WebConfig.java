package com.mycuckoo.flow.config;

import com.google.common.collect.Lists;
import com.mycuckoo.core.web.filter.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
public class WebConfig {
    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);


    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter((account, usr) -> Collections.emptyList());
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Lists.newArrayList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);

        return new OrderCorsFilter(source);
    }

    @Order(0)
    public static class OrderCorsFilter extends CorsFilter {
        public OrderCorsFilter(CorsConfigurationSource configSource) {
            super(configSource);
        }
    }
}

