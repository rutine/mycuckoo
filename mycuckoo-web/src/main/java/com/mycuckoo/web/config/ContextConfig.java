package com.mycuckoo.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycuckoo.repository.PageInterceptor;
import com.mycuckoo.web.util.JsonUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;

@Configuration
@ComponentScan("com.mycuckoo.service")
@MapperScan("com.mycuckoo.repository")
public class ContextConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Interceptor interceptors() {
        return new PageInterceptor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = JsonUtils.newMapper(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        return objectMapper;
    }

}
