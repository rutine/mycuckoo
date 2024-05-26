package com.mycuckoo.core.repository.plugin;

import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.core.repository.PageQuery;
import com.mycuckoo.core.repository.Pageable;
import com.mycuckoo.core.repository.auth.PreAuthBeanPostProcessor;
import com.mycuckoo.core.repository.param.QueryContext;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:38
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class PageInterceptor extends PreAuthBeanPostProcessor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

    private String countSuffix = "_count";

    private Map<String, MappedStatement> countMsCache = null;
    protected ThreadLocal<Pageable> pageThreadLocal = new ThreadLocal<>();
    protected CountSqlParser parser = new CountSqlParser();

    private static Field additionalParametersField;

    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("获取 BoundSql 属性 additionalParameters 失败", e);
        }
    }


    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement)args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds)args[2];
        ResultHandler resultHandler = (ResultHandler)args[3];
        Executor executor = (Executor)invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        Pageable page = find(parameter, Pageable.class);
        QueryContext context = new CreateQueryContext().create(ms.getConfiguration(), parameter);
        if (args.length == 4) {
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            cacheKey = (CacheKey)args[4];
            boundSql = (BoundSql)args[5];
        }

//        PreAuthInfo auth = authMap.get(ms.getId());
//        RowResolver rowResolver = auth == null ? null : RowResolver.from(auth, this.find(parameter, PreAuthInfo.class));
//        if (context != null || rowResolver != null) {
//            SqlEnhancer enhancer = new SqlEnhancer(ms.getConfiguration(), parameter, boundSql, context, rowResolver);
//            boundSql = enhancer.enhance();
//
//            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
//        }
//        return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);

        if (page == null) {
            logger.debug("------> 没有分页参数, 不是分页查询");
            return invocation.proceed();
        }
        logger.debug("------> 检测到分页参数, 使用分页查询");

        Object obj = pageThreadLocal.get();
        if (obj != null) {
            return invocation.proceed();
        }

        pageThreadLocal.set(page);
        try {
            long count = 0;
            if (page.getPageSize() > 0) {
                count = this.count(executor, ms, parameter, (ResultHandler) null, boundSql);
            }
            List result = new ArrayList(0);
            if (count > 0 || page.getPageSize() == 0) {
                result = this.query(executor, ms, parameter, resultHandler, boundSql, cacheKey);
            }

            return Arrays.asList(new PageImpl<>(result, page, count));
        } finally {
            pageThreadLocal.remove();
        }
    }

    private Long count(Executor executor, MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        String countMsId = ms.getId() + this.countSuffix;
        MappedStatement countMs = this.getExistedMappedStatement(ms.getConfiguration(), countMsId);
        Long count;
        if (countMs != null) {
            count = this.doManualCount(executor, countMs, parameter, resultHandler, boundSql);
        } else {
            if (this.countMsCache != null) {
                countMs = this.countMsCache.get(countMsId);
            }

            if (countMs == null) {
                countMs = this.newMappedStatement(ms, countMsId);
                if (this.countMsCache != null) {
                    this.countMsCache.put(countMsId, countMs);
                }
            }

            count = this.doAutoCount(executor, countMs, parameter, resultHandler, boundSql);
        }

        return count;
    }
    private MappedStatement getExistedMappedStatement(Configuration configuration, String msId) {
        try {
            return configuration.getMappedStatement(msId, false);
        } catch (Throwable throwable) {

        }

        return null;
    }
    private Long doManualCount(Executor executor, MappedStatement countMs, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        BoundSql countBoundSql = countMs.getBoundSql(parameter);
        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = ((Number)((List)countResultList).get(0)).longValue();

        return count;
    }
    private Long doAutoCount(Executor executor, MappedStatement countMs, Object parameter, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        String countSql = this.getCountSql(boundSql.getSql());
        final BoundSql newBoundSql = new BoundSql(countMs.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);

        Map<String, Object> additionalParameters = this.getAdditionalParameter(boundSql);
        additionalParameters.forEach((key, value) -> {
            newBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        });

        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, newBoundSql);
        Long count = (Long)((List)countResultList).get(0);

        return count;
    }
    private Map<String, Object> getAdditionalParameter(BoundSql boundSql) {
        try {
            //3.4.0后, MyBatis分页foreach list item 通过次参数获取
            return (Map)additionalParametersField.get(boundSql);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取 BoundSql 属性值 additionalParameters 失败", e);
        }
    }
    private MappedStatement newMappedStatement(MappedStatement ms, String id) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), id, ms.getSqlSource(), ms.getSqlCommandType());
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
    private <E> List<E> query(Executor executor, MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql, CacheKey cacheKey) throws SQLException {
        String pageSql = this.getPageSql(pageThreadLocal.get(), boundSql.getSql());
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);

        Map<String, Object> additionalParameters = this.getAdditionalParameter(boundSql);
        additionalParameters.forEach((key, value) -> {
            newBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        });

        return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, newBoundSql);
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

    private String getCountSql(String sql) {
        return parser.getSmartCountSql(sql);
    }

    private String getPageSql(Pageable page, String sql) {
        if (page.getPageSize() == 0) {
            return sql;
        }
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(" limit ")
                .append(page.getPageSize())
                .append(" offset ")
                .append(page.getOffset());

        return sqlBuilder.toString();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        countMsCache = new ConcurrentHashMap<>();
    }


    private class CreateQueryContext {
        protected QueryContext create(Configuration configuration, Object parameterObject) {
            PageQuery query = find(parameterObject, PageQuery.class);
            if (query != null) {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                if (!metaObject.hasGetter("keyword")) {
                    metaObject.setValue("keyword", query.getKeyword());
                }
                if (!metaObject.hasGetter("q")) {
                    metaObject.setValue("q", query.getQ());
                }
                if (!metaObject.hasGetter("sort")) {
                    metaObject.setValue("sort", query.getSort());
                }
                if (!metaObject.hasGetter("where")) {
                    metaObject.setValue("where", query.getWhere());
                }
                if (query.getColumns() != null && !query.getColumns().isEmpty()) {
                    return new QueryContext.Builder()
                            .columns(query.getColumns())
                            .where(query.getWhere())
                            .sort(query.getSort())
                            .aggr(query.getAggr())
                            .deduplicate(query.getDistinct())
                            .build();
                }
            }

            return null;
        }

        private <T> T get(MetaObject paramsObject, String paramName) {
            Object value = null;
            if (paramsObject.hasGetter(paramName)) {
                value = paramsObject.getValue(paramName);
            }
            return (T) value;
        }
    }
}

