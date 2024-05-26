package com.mycuckoo.core.repository.auth;

import com.mycuckoo.core.repository.annotation.PreAuth;
import com.mycuckoo.core.repository.param.PreAuthInfo;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明: 收集权限配置Bean处理器
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:18
 */
public class PreAuthBeanPostProcessor implements BeanPostProcessor {
    protected Map<String, PreAuthInfo> authMap = new ConcurrentHashMap<>();

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MapperFactoryBean) {
            Class<?> mapper = ((MapperFactoryBean) bean).getMapperInterface();
            PreAuth baseAuth = mapper.getAnnotation(PreAuth.class);
            ReflectionUtils.doWithMethods(mapper,
                    method -> authMap.compute(mapper.getCanonicalName() + "." + method.getName(), (key, value) -> {
                        String table = null;
                        String alias = null;
                        String column = null;
                        if (baseAuth != null) {
                            table = baseAuth.table();
                            alias = baseAuth.alias();
                            column = baseAuth.column();
                        }
                        PreAuth preAuth = method.getAnnotation(PreAuth.class);
                        if (preAuth != null) {
                            if (preAuth.table() != null) {
                                table = preAuth.table();
                            }
                            if (preAuth.alias() != null) {
                                alias = preAuth.alias();
                            }
                            if (preAuth.column() != null) {
                                column = preAuth.column();
                            }
                        }

                        return new PreAuthInfo(table, alias, column);
                    }),
                    method -> baseAuth != null || method.getAnnotation(PreAuth.class) != null );
        }

        return bean;
    }
}

