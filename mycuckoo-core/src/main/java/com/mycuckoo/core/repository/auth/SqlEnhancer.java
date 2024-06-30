package com.mycuckoo.core.repository.auth;

import com.mycuckoo.core.repository.param.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:36
 */
public class SqlEnhancer {
    private static Logger logger = LoggerFactory.getLogger(SqlEnhancer.class);
    private static Field additionalParametersField;
    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            logger.error("sql enhancer error", e);
        }
    }

    private Configuration configuration;
    private Object parameter;
    private BoundSql boundSql;
    private QueryContext queryContext;
    private RowResolver rowResolver;

    private Statement stmt;
    private List<ParameterMapping> parameterMappings;
    private Map<String, Object> additionalParameters;
    private int paramIndex;


    public SqlEnhancer(Configuration configuration, Object parameter, BoundSql boundSql, QueryContext context, RowResolver rowResolver)
            throws IllegalArgumentException, IllegalAccessException {
        this.configuration = configuration;
        this.parameter = parameter;
        this.boundSql = boundSql;
        this.queryContext = context;
        this.rowResolver = rowResolver;

        this.init();
    }
    private void init() throws IllegalArgumentException, IllegalAccessException {
        try {
            Statement stmt = CCJSqlParserUtil.parse(boundSql.getSql());
            this.stmt = stmt;
//            if (stmt instanceof PlainSelect) {
//                plainSelect = (PlainSelect) stmt;
//            } else if (stmt instanceof ParenthesedSelect && ((ParenthesedSelect) stmt).getSelect() instanceof PlainSelect) {
//                plainSelect = ((ParenthesedSelect) stmt).getPlainSelect();
//            }
        } catch (JSQLParserException e) {
            // 无法解析的用一般方法返回count语句
            logger.error("can't enhance original sql: {}", boundSql.getSql(), e);
        }

        //在最初始条件默认参数的情况下（即没有被if等条件语句包含的参数），mybatis会自动填充一个不可修改的list，代码见MappedStatement.getBoundSql方法
        //这样再调用add方法会报错，所以这里新建一个list
        parameterMappings = new ArrayList<>(boundSql.getParameterMappings());
        additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
        paramIndex = parameterMappings.size();
    }

    public BoundSql enhance() {
        if (stmt instanceof PlainSelect) {
            return this.enhance((PlainSelect) stmt);
        } else if (stmt instanceof ParenthesedSelect && ((ParenthesedSelect) stmt).getSelect() instanceof PlainSelect) {
            return this.enhance(((ParenthesedSelect) stmt).getPlainSelect());
        } else if (stmt instanceof Update || stmt instanceof Delete) {
            return this.enhance(stmt);
        }

        return boundSql;
    }

    public BoundSql enhance(Statement stmt) {
        if (stmt == null) {
            return boundSql;
        }

        UpdateRowSqlBuilder sqlBuilder = new UpdateRowSqlBuilder((rowResolver != null && rowResolver.isPreFiltered()) ? rowResolver.getSelect() : stmt, paramIndex, rowResolver);
        sqlBuilder.row(this::addParameterMapping);

        BoundSql newBoundSql = new BoundSql(configuration, sqlBuilder.build(), parameterMappings, parameter);
        additionalParameters.forEach(newBoundSql::setAdditionalParameter);

        return newBoundSql;
    }

    public BoundSql enhance(PlainSelect plainSelect) {
        if (plainSelect == null) {
            return boundSql;
        }

        SelectRowSqlBuilder sqlBuilder = new SelectRowSqlBuilder((rowResolver != null && rowResolver.isPreFiltered()) ? rowResolver.getSelect() : plainSelect, paramIndex, rowResolver);
        if (queryContext != null) {
            if (queryContext.getDeduplicate() != null && !queryContext.getDeduplicate().trim().isEmpty()) {
                return this.enhanceDistinct(plainSelect);
            } else if (queryContext.isSkip() && rowResolver == null) {
                //原样输出
                return boundSql;
            }

            if (!queryContext.isSkip()) {
                queryContext.getAggr().forEach(sqlBuilder::selectItem);

                for (WhereParam queryParam : queryContext.getWhere()) {
                    String type = queryParam.getFilterType();
                    if (FilterType.EQ.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.text(queryParam, this::addParameterMapping);
                    } else if (FilterType.LIKE.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.like(queryParam, false, this::addParameterMapping);
                    } else if (FilterType.TEXT.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.like(queryParam, true, this::addParameterMapping);
                    } else if (FilterType.SCOPE.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.scope(queryParam, this::addParameterMapping);
                    } else if (FilterType.MULTI.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.multi(queryParam, this::addParameterMapping);
                    } else if (FilterType.PROVINCE_CITY.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.district(queryParam, this::addParameterMapping);
                    } else if (FilterType.PROVINCE_CITY_AREA.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.district(queryParam, this::addParameterMapping);
                    } else if (FilterType.BOOL.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.bool(queryParam, this::addParameterMapping);
                    }
                }

                SortParam sortParam = queryContext.getSort();
                if (sortParam != null) {
                    String type = sortParam.getFilterType();
                    String field = sortParam.getField();
                    boolean asc = sortParam.isAsc();
                    if (FilterType.PROVINCE_CITY.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.sort("province", asc);
                        sqlBuilder.sort("city", asc);
                    } else if (FilterType.PROVINCE_CITY_AREA.getCode().equalsIgnoreCase(type)) {
                        sqlBuilder.sort("province", asc);
                        sqlBuilder.sort("city", asc);
                        sqlBuilder.sort("area", asc);
                    }
//                    else if (FilterType.TIMESCOPE.equals(type)) {
//                        sqlBuilder.sort(String.format("%sStart", field), asc);
//                    }
                    else {
                        sqlBuilder.sort(field, asc);
                    }
                }
            }
        }
        sqlBuilder.row(this::addParameterMapping);

        BoundSql newBoundSql = new BoundSql(configuration, sqlBuilder.build(), parameterMappings, parameter);
        additionalParameters.forEach(newBoundSql::setAdditionalParameter);

        return newBoundSql;
    }

    private BoundSql enhanceDistinct(PlainSelect plainSelect) {
        String deduplicate = queryContext.getDeduplicate();
        //验证有效性，无效返回原先的BoundSql对象
        Column col = queryContext.getColumn(deduplicate);
        if (col != null && (col.getFilterType().equalsIgnoreCase(FilterType.TEXT.getCode()) || col.getFilterType().equalsIgnoreCase(FilterType.MULTI.getCode()))) {
            SelectRowSqlBuilder sqlBuilder = new SelectRowSqlBuilder(plainSelect, paramIndex, rowResolver);
            sqlBuilder.distinct(deduplicate);
            sqlBuilder.row(this::addParameterMapping);

            BoundSql newBoundSql = new BoundSql(configuration, sqlBuilder.build(), parameterMappings, parameter);
            additionalParameters.forEach(newBoundSql::setAdditionalParameter);

            return newBoundSql;
        } else {
            return this.boundSql;
        }
    }

    private void addParameterMapping(String property, Object obj) {
        Class<?> type = obj instanceof Class ? (Class) obj : obj.getClass();
        parameterMappings.add(new ParameterMapping.Builder(configuration, property, type).build());

        if (!(obj instanceof Class)) {
            additionalParameters.put(property, obj);
        }
    }
}

