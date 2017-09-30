package com.mycuckoo.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static com.mycuckoo.common.constant.Common.WEB_APP_ROOT_KEY;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter implements ServletContextInitializer {
	private static Logger logger = LoggerFactory.getLogger(WebConfig.class);
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor())
			.addPathPatterns("/**");
//			.excludePathPatterns("/login/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowedMethods("POST", "PUT", "GET", "DELETE")
			.allowCredentials(true);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addListener(WebAppRootListener.class);
		servletContext.setInitParameter("webAppRootKey", WEB_APP_ROOT_KEY); //这里是注入参数的名称
	}
}
