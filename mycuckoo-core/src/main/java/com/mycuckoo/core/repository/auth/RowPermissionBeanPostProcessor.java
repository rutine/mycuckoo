package com.mycuckoo.core.repository.auth;

import com.mycuckoo.core.annotation.RowPermission;
import com.mycuckoo.core.repository.param.RowPermissionInfo;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明: 收集行权限配置Bean处理器
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:18
 */
public class RowPermissionBeanPostProcessor implements BeanPostProcessor {
    protected Map<String, RowPermissionInfo> authMap = new ConcurrentHashMap<>();

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MapperFactoryBean) {
            Class<?> mapper = ((MapperFactoryBean) bean).getMapperInterface();
            RowPermission baseAuth = mapper.getAnnotation(RowPermission.class);
            ReflectionUtils.doWithMethods(mapper,
                    method -> authMap.compute(mapper.getCanonicalName() + "." + method.getName(), (key, value) -> {
                        String table = null;
                        String alias = null;
                        String tenant = null;
                        String user = null;
                        Integer row = null;
                        if (baseAuth != null) {
                            table = baseAuth.table();
                            alias = baseAuth.alias();
                            tenant = baseAuth.tenant();
                            user = baseAuth.user();
                            row = baseAuth.row().getValue();
                        }
                        RowPermission preAuth = method.getAnnotation(RowPermission.class);
                        if (preAuth != null) {
                            if (preAuth.table() != null) {
                                table = preAuth.table();
                            }
                            if (preAuth.alias() != null) {
                                alias = preAuth.alias();
                            }
                            if (preAuth.tenant() != null) {
                                tenant = preAuth.tenant();
                            }
                            if (preAuth.user() != null) {
                                user = preAuth.user();
                            }
                            if (preAuth.row() != null) {
                                if (preAuth.row().getValue() == 0) {
                                    row = 0;
                                } else {
                                    row |= preAuth.row().getValue();
                                }
                            }
                        }

                        return new RowPermissionInfo(table, alias, tenant, user, row);
                    }),
                    method -> baseAuth != null || method.getAnnotation(RowPermission.class) != null );
        }

        return bean;
    }
}

