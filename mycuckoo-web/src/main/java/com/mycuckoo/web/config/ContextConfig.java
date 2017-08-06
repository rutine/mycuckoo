package com.mycuckoo.web.config;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.mycuckoo.repository.PageInterceptor;

@Configuration
@MapperScan("com.mycuckoo.repository")
@ComponentScan("com.mycuckoo.service")
public class ContextConfig {
	
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	public Interceptor interceptors() {
		return new PageInterceptor();
	}

}
