package com.mycuckoo.web.config;

import com.google.common.collect.Lists;
import com.mycuckoo.core.web.filter.JwtFilter;
import com.mycuckoo.core.web.filter.PrivilegeFilter;
import com.mycuckoo.service.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static com.mycuckoo.constant.BaseConst.WEB_APP_ROOT_KEY;

@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class WebConfig implements WebMvcConfigurer, ServletContextInitializer {
    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**", "/view/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/static/", "classpath:/META-INF/resources/webjars/view/");
    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/static/**",
//                        "/view/**",
//                        "/swagger-resources/**",
//                        "/v2/api-docs/**",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/**/*.png"
//                );
//    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedHeaders("*")
//                .allowedMethods("*")
//                .allowCredentials(true);
//    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info(" ====== set webAppRootKey={} ======", WEB_APP_ROOT_KEY);

        servletContext.addListener(WebAppRootListener.class);
        servletContext.setInitParameter("webAppRootKey", WEB_APP_ROOT_KEY); //这里是注入参数的名称
    }

    @Bean
    public JwtFilter jwtFilter(LoginService service) {
        return new JwtFilter((account, usr) -> service.getUserResources(usr.getId(), usr.getRoleId(), usr.getOrgId(), account));
    }

    @Bean
    public PrivilegeFilter privilegeFilter(WebProperties properties, LoginService service) {
        return new PrivilegeFilter(
                properties.getAllowPaths().toArray(new String[] {}),
                properties.getSessionPaths().toArray(new String[] {}),
                () -> service.getAllResources());
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
