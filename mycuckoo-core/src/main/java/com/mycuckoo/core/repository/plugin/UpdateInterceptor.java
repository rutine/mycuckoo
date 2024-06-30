package com.mycuckoo.core.repository.plugin;

import com.mycuckoo.core.repository.auth.PreAuthBeanPostProcessor;
import com.mycuckoo.core.repository.auth.RowResolver;
import com.mycuckoo.core.repository.auth.SqlEnhancer;
import com.mycuckoo.core.repository.param.PreAuthInfo;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/27 20:49
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class UpdateInterceptor extends PreAuthBeanPostProcessor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(UpdateInterceptor.class);

    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql = ms.getBoundSql(parameter);
        if (ms.getSqlCommandType() != SqlCommandType.DELETE && ms.getSqlCommandType() != SqlCommandType.UPDATE) {
            return invocation.proceed();
        }

        PreAuthInfo auth = authMap.get(ms.getId());
        RowResolver rowResolver = auth == null || auth.getRow() == 0 ? null : RowResolver.from(auth, this.find(parameter, PreAuthInfo.class));
        if (rowResolver != null) {
            SqlEnhancer enhancer = new SqlEnhancer(ms.getConfiguration(), parameter, boundSql, null, rowResolver);
            boundSql = enhancer.enhance();
            MappedStatement newStatement = newMappedStatement(ms, boundSql);
            args[0] = newStatement;
        }

        return invocation.proceed();
    }


    private MappedStatement newMappedStatement(MappedStatement ms, BoundSql boundSql) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), new RowSqlSource(boundSql), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            builder.keyProperty(Arrays.stream(ms.getKeyProperties()).collect(Collectors.joining(",")));
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(Arrays.asList((new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, Collections.EMPTY_LIST)).build()));
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
    private <T> T find(Object parameterObject, Class<T> clazz) {
        if (clazz.isInstance(parameterObject)) {
            return (T) parameterObject;
        } else if (parameterObject instanceof Map) {
            Optional<T> optional = ((Map) parameterObject).values().stream().filter(clazz::isInstance).findFirst();

            return optional.isPresent() ? optional.get() : null;
        }

        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    static class RowSqlSource implements SqlSource {
        private BoundSql boundSql;

        public RowSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object o) {
            return boundSql;
        }
    }
}

