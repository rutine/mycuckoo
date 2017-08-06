package com.mycuckoo.web.config;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	private static Logger logger = LoggerFactory.getLogger(WebConfig.class);
	
	@Bean
	public HandlerExceptionResolver globalHandlerExceptionResolver() {
		return new HandlerExceptionResolver() {

			@Override
			public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
					Object handler, Exception ex) {
				logger.info("全局异常处理", ex);
				try {
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=UTF-8");
					AjaxResponse<?> result = null;
					if(ex instanceof ApplicationException) {
						ApplicationException exx = (ApplicationException) ex;
						result = AjaxResponse.create(exx.getCode(), exx.getMessage());
					} else if(ex instanceof SystemException) {
						SystemException exx = (SystemException) ex;
						result = AjaxResponse.create(500, exx.getMessage());
					} else {
						result = AjaxResponse.create(500, "系统异常");
					}
					
					PrintWriter writer = response.getWriter();
					writer.write(JsonUtils.toJson(result));
					writer.flush();
					writer.close();
				} catch (Exception e) {
					logger.error("全局异常处理错误", e);
				}
				
				return null;
			}
			
		};
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInteceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/login");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowedMethods("POST")
			.allowCredentials(true);
	}
}
