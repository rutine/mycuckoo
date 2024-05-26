package com.mycuckoo.core.repository.plugin;

import com.google.common.collect.Maps;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.core.repository.Pageable;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 功能说明: 分页拦截插件
 *
 * @author rutine
 * @version 3.0.0
 * @time Jun 18, 2017 11:05:25 AM
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class PageInterceptorOld implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(PageInterceptorOld.class);

    protected ThreadLocal<ThreadLocalPage> pageThreadLocal = new ThreadLocal<>();
    protected CountSqlParser parser = new CountSqlParser();


    public Object intercept(Invocation invocation) throws Throwable {
        // 控制SQL和查询总数的地方
        if (invocation.getTarget() instanceof StatementHandler) {
            ThreadLocalPage threadLocalPage = pageThreadLocal.get();
            if (threadLocalPage == null) { // 不是分页查询
                return invocation.proceed();
            }

            RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
            StatementHandler delegate = (StatementHandler) FieldUtils.readField(handler, "delegate", true);
            BoundSql boundSql = delegate.getBoundSql();
            if (threadLocalPage.totalElements > -1) {
                logger.debug("------> 已经设置了总页数, 不需要再查询总数");
            } else {
                Object parameterObj = boundSql.getParameterObject();
                MappedStatement mappedStatement = (MappedStatement) FieldUtils.readField(delegate, "mappedStatement", true);
                Connection connection = (Connection) invocation.getArgs()[0];
                long totalElements = this.getTotalRecord(parameterObj, mappedStatement, connection);
                threadLocalPage.totalElements = totalElements;
            }

            String sql = boundSql.getSql();
            String pageSql = this.getPageSql(threadLocalPage.page, sql);
            FieldUtils.writeField(boundSql, "sql", pageSql, true);
            Object obj = invocation.proceed();

            return obj;
        }
        // 查询结果的地方 获取是否有分页Page对象
        else {
            Pageable page = findPageObject(invocation.getArgs()[1]);
            if (page == null) {
                logger.debug("------> 没有分页参数, 不是分页查询");
                return invocation.proceed();
            }
            logger.debug("------> 检测到分页参数, 使用分页查询");

            // 设置真正的parameterObj
            invocation.getArgs()[1] = extractRealParameterObject(invocation.getArgs()[1]);

            ThreadLocalPage threadLocalPage = new ThreadLocalPage();
            threadLocalPage.page = page;
            pageThreadLocal.set(threadLocalPage);
            try {
                Object resultObj = invocation.proceed(); // Executor.query(..)
                if (resultObj instanceof List) {
                    ClassLoader classLoader = invocation.getClass().getClassLoader();
                    Class<?>[] interfaces = new Class<?>[]{List.class, Page.class, Serializable.class};
                    InvocationHandler handler = new PageInvocationHandler(new PageImpl<>((List<?>) resultObj, page, threadLocalPage.totalElements));

                    return Proxy.newProxyInstance(classLoader, interfaces, handler);
                }

                return resultObj;
            } finally {
                pageThreadLocal.remove();
            }
        }
    }

    /**
     * 给当前的参数对象page设置总记录数
     *
     * @param parameterObject Mapper映射语句对应的参数对象
     * @param mappedStatement Mapper映射语句
     * @param connection      当前的数据库连接
     */
    private long getTotalRecord(Object parameterObject, MappedStatement mappedStatement, Connection connection) {
        // 获取对应的BoundSql，这个BoundSql跟利用StatementHandler获取到的BoundSql是同一个
        // delegate里的boundSql也是通过mappedStatement.getBoundSql(paramObj)方法获取
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        String countSql = this.getCountSql(sql);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings(); // 获取对应的参数映射
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, parameterObject);


        try {
            //解决MyBatis 分页foreach list item问题！ 升级3.4.0后的问题 此部分为新增内容
            Object additionalParameters = FieldUtils.readField(boundSql, "additionalParameters", true);
            if (additionalParameters != null) {
                FieldUtils.writeField(countBoundSql, "additionalParameters", additionalParameters, true);
            }
            //升级3.4.0后的问题 此部分为新增内容 end

            //解决MyBatis 分页foreach 参数失效 start
            MetaObject metaParameters = (MetaObject) FieldUtils.readField(boundSql, "metaParameters", true);
            if (metaParameters != null) {
                FieldUtils.writeField(countBoundSql, "metaParameters", metaParameters, true);
            }
            //解决MyBatis 分页foreach 参数失效 end
        } catch (Exception e) {
            logger.error("------> 解决foreach参数失效失败", e);
        }


        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBoundSql);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(countSql);
            parameterHandler.setParameters(pstmt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalRecord = rs.getInt(1);
                return totalRecord;
            }
        } catch (SQLException e) {
            logger.error("------> 查询总记录数失败", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("------> 关闭连接失败", e);
            }
        }

        return 0;
    }

    protected Pageable findPageObject(Object parameterObj) {
        if (parameterObj instanceof Pageable) {
            return (Pageable) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof Pageable) {
                    return (Pageable) val;
                }
            }
        }
        return null;
    }

    /**
     * <pre>
     * 把真正的参数对象解析出来
     * Spring会自动封装参数对象为Map<String, Object>对象
     * 对于通过@Param指定key值参数我们不做处理，因为XML文件需要该KEY值
     * 而对于没有@Param指定时，Spring会使用0,1作为主键
     * 对于没有@Param指定名称的参数,一般XML文件会直接对真正的参数对象解析，
     * 此时解析出真正的参数作为根对象
     * </pre>
     *
     * @param parameterObj
     * @return
     */
    protected Object extractRealParameterObject(Object parameterObj) {
        if (parameterObj instanceof Map) {
            Map<String, Object> parameterMap = (Map<String, Object>) parameterObj;
            Iterator<Map.Entry<String, Object>> it = parameterMap.entrySet().iterator();
            Map<String, Object> myParameterMap = Maps.newHashMap();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                String keyStr = entry.getKey();
                Object valueObj = entry.getValue();
                if (valueObj instanceof Pageable) {
                    continue;
                }
                else if (valueObj instanceof Map) {
                    myParameterMap.putAll((Map) valueObj);
                }
                else {
                    myParameterMap.put(keyStr, valueObj);
                }
            }
            return myParameterMap;
        }

        return parameterObj;
    }

    private String getCountSql(String sql) {
        return parser.getSmartCountSql(sql);
    }

    private String getPageSql(Pageable page, String sql) {
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

    }

    private static class ThreadLocalPage {
        private Pageable page;
        private long totalElements = -1;
    }

    private static class PageInvocationHandler implements InvocationHandler, Serializable {
        private final Page<?> page;

        public PageInvocationHandler(Page<?> page) {
            this.page = page;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("get")) {
                return this.page;
            } else if (method.getName().equals("size")) {
                return 1;
            }

            try {
                return method.invoke(this.page.getContent(), args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }
}
