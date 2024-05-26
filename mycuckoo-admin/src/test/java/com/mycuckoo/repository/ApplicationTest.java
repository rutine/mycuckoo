package com.mycuckoo.repository;

import com.mycuckoo.core.repository.plugin.PageInterceptorOld;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan("com.mycuckoo.repository")
public class ApplicationTest {
    private static Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Interceptor interceptors() {
        return new PageInterceptorOld();
    }

    /**
     * Main Start
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class, args);
        logger.info("============= Mycuckoo Start Success =============");
    }
}