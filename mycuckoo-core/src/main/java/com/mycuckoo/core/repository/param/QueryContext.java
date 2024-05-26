package com.mycuckoo.core.repository.param;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:24
 */
public class QueryContext {
    public static final String VALUE = "value";
    public static final String WITH_BANK = "withBank";

    private QueryContext.ColumnSource source;
    private List<? extends Column> columns;
    private Map<String, Object> where;
    private Map<String, String> sort;
    private Map<String, String> aggr = new HashMap<>();
    private String deduplicate;


    public boolean isSkip() {
        return columns.isEmpty() || (getAggr().isEmpty() && getWhere().isEmpty() && getSort() == null);
    }

    public boolean containColumn(String field) {
        return source.containsProperty(field);
    }

    public Column getColumn(String field) {
        return this.source.getProperty(field);
    }

    public List<WhereParam> getWhere() {
        List<WhereParam> params = new ArrayList<>();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            Column col = null;
            if ((col = source.getProperty(entry.getKey())) != null && col.isFilter()) {
                WhereParam queryParam = this.getQueryParam(entry.getValue(), col);
                if (queryParam != null) {
                    params.add(queryParam);
                }
            }
        }

        return params;
    }

    public SortParam getSort() {
        for (Map.Entry<String, String> entry : sort.entrySet()) {
            Column col = null;
            if (entry.getValue() != null && (col = source.getProperty(entry.getKey())) != null && col.isSort()) {
                return new SortParam(col, !"desc".equalsIgnoreCase(entry.getValue()));
            }
        }

        return null;
    }

    public List<AggrParam> getAggr() {
        List<AggrParam> aggrParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : aggr.entrySet()) {
            Column col = null;
            if ((col = source.getProperty(entry.getKey())) != null) {
                aggrParams.add(new AggrParam(col, entry.getValue()));
            }
        }

        return aggrParams;
    }

    public String getDeduplicate() {
        return deduplicate;
    }


    private WhereParam getQueryParam(Object object, Column column) {
        //todo 类型转换
        if (object != null) {
            return new WhereParam(column, object);
        }

        return null;
    }


    // =========== inner use class ==========
    public static class Builder {
        private QueryContext context;

        public Builder() {
            this.context = new QueryContext();
        }

        public Builder columns(List<? extends Column> columns) {
            this.context.columns = columns != null ? columns : new ArrayList<>(0);
            return this;
        }

        public Builder where(Map<String, Object> where) {
            this.context.where = where != null ? where : new HashMap<>(0);
            return this;
        }

        public Builder sort(Map<String, String> sort) {
            this.context.sort = sort != null ? sort : new HashMap<>(0);
            return this;
        }

        public Builder aggr(Map<String, String> aggr) {
            this.context.aggr = aggr != null ? aggr : new HashMap<>(0);
            return this;
        }

        public Builder deduplicate(String deduplicate) {
            this.context.deduplicate = deduplicate;
            return this;
        }

        public QueryContext build() {
            this.context.source = ColumnSource.from(this.context.columns);
            return this.context;
        }
    }

    static class ColumnSource extends EnumerablePropertySource<Map<String, ? extends Column>> {
        public ColumnSource(Map<String, ? extends Column> source) {
            this("columns", source);
        }

        public ColumnSource(String name, Map<String, ? extends Column> source) {
            super(name, source);
        }

        @Override
        public String[] getPropertyNames() {
            return StringUtils.toStringArray(this.getSource().keySet());
        }

        @Nullable
        @Override
        public Column getProperty(String field) {
            return this.source.get(field);
        }

        static ColumnSource from(List<? extends Column> columns) {
            //Column可能会因为错误的配置导致field重复，这里归并时去掉重复的元素
            Map<String, Column> mapper = columns.stream()
                    .collect(Collectors.groupingBy(Column::getField,
                            Collectors.reducing(null, Function.identity(), (t1, t2) -> t2 != null ? t2 : t1)));
            return new ColumnSource(mapper);
        }
    }
}