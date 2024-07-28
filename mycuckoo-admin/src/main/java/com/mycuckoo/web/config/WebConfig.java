package com.mycuckoo.web.config;

import com.mycuckoo.core.web.filter.PrivilegeFilter;
import com.mycuckoo.service.platform.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.List;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.BaseConst.WEB_APP_ROOT_KEY;

@Configuration
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("POST", "PUT", "GET", "DELETE")
                .allowCredentials(true);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info(" ====== set webAppRootKey={} ======", WEB_APP_ROOT_KEY);

        servletContext.addListener(WebAppRootListener.class);
        servletContext.setInitParameter("webAppRootKey", WEB_APP_ROOT_KEY); //这里是注入参数的名称
    }



    @Bean
    public PrivilegeFilter privilegeFilter(ModuleService service) {
        String[] allowPaths = {
                "/register",
                "/login",
                "/login/logout",
                "/captcha/**",
                "/file/**",
                "/static/**",
                "/view/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png"
        };
        String[] sessionPaths = {
                "/login/orgs",
                "/login/menus",
                "/platform/config/list-table-config",
                "/platform/system/dictionary/mgr/small-type"
        };

        List<PrivilegeFilter.ResourceInfo>  resources = service.findAllModResRefs().stream()
                .map(o -> new PrivilegeFilter.ResourceInfo(o.getPath(), o.getMethod(), o.getId().toString()))
                .collect(Collectors.toList());
        return new PrivilegeFilter(allowPaths, sessionPaths, resources);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Access-Control-Request-Method");
        config.addAllowedHeader("Access-Control-Request-Headers");
        config.addAllowedHeader("Access-Control-Allow-Origin");
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);

        return new OrderCorsFilter(source);
    }

    @Order(2)
    public static class OrderCorsFilter extends CorsFilter {
        public OrderCorsFilter(CorsConfigurationSource configSource) {
            super(configSource);
        }
    }
}
