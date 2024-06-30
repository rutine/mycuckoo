package com.mycuckoo.core;

import com.mycuckoo.core.repository.PageQuery;
import com.mycuckoo.core.repository.Pageable;
import com.mycuckoo.core.repository.param.Column;
import com.mycuckoo.core.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能说明: 查询器
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:22
 */
public class Querier implements PageQuery, Pageable {
    public static final Querier EMPTY = new Querier(0, 0);

    private Integer pageNo;
    private Integer pageSize;

    private String tableCode;
    private List<? extends Column> columns;

    private String keyword;
    private Map<String, Object> q = new HashMap<>();
    private Map<String, Object> where;
    private Map<String, String> sort;
    private Map<String, String> aggr;
    private String distinct;


    public Querier() {}

    public Querier(int page, int size) {
        this.pageNo = page;
        this.pageSize = size;
    }

    @Deprecated
    @Override
    public int getPageNumber() {
        return this.getPageNo();
    }

    public int getPageNo() {
        if (pageNo == null) {
            return 1;
        } else if (pageNo <= 0) {
            return 0;
        } else {
            return pageNo;
        }
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        if (pageSize == null) {
            return 10;
        } else if (pageSize <= 0) {
            return 0;
        } else {
            return pageSize;
        }
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int getOffset() {
        return (this.getPageNo() - 1) * this.getPageSize();
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode == null ? null : tableCode.trim();
    }

    public List<? extends Column> getColumns() {
        return columns;
    }

    public String getKeyword() {
        return StringUtils.trimToNull(keyword);
    }

    public void setKeyword(String keyword) {
        if (keyword != null) {
            String word = keyword.trim();
            if (word.length() > 100) {
                word = word.substring(0, 100);
            }
            this.keyword = word;
        } else {
            this.keyword = keyword;
        }
    }

    public String getDistinct() {
        return distinct;
    }

    public void setDistinct(String distinct) {
        this.distinct = distinct == null ? null : distinct.trim();
    }

    public Map<String, Object> getWhere() {
        if (where == null) {
            where = new HashMap<>();
        }
        return where;
    }

    public void setWhere(Map<String, Object> where) {
        this.where = where;
    }

    public Map<String, String> getSort() {
        if (sort == null) {
            sort = new HashMap<>();
        }
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    public Map<String, Object> getQ() {
        if (q != null) {
            this.q = q.entrySet().stream()
                    .filter(entry -> Objects.nonNull(entry.getValue()) && entry.getValue() instanceof String && !CommonUtils.isEmpty((String) entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return q;
    }

    public void setQ(Map<String, Object> q) {
       this.q = q;
    }

    public Object getRequired(String key) {
        return q.get(key);
    }

//    public <T> T getRequired(String key, Class<T> clazz) {
//        return (T) ConvertUtil.convert(q.get(key), clazz);
//    }

    public void putQ(String key, Object value) {
        if (CommonUtils.isNotBlank(key) && value != null) {
            this.q.put(key, value);
        }
    }

    public Map<String, String> getAggr() {
        return aggr == null ? Collections.EMPTY_MAP : aggr;
    }

    public Querier copy(Map<String, String> aggr) {
        return new CloneQuerier(this, aggr);
    }

    private class CloneQuerier extends Querier implements PageQuery {
        private Querier querier;
        private List<? extends Column> columns;
        private Map<String, String> aggr;

        CloneQuerier(Querier querier) {
            this.querier = querier;
        }

        CloneQuerier(Querier querier, Map<String, String> aggr) {
            this.querier = querier;
            this.aggr = aggr;

            //聚合查询, 不执行分页
            this.querier.setPageSize(0);
            this.querier.setPageNo(0);
        }

        CloneQuerier(Querier querier, List<? extends Column> columns) {
            this.querier = querier;
            this.columns = columns;
        }

        @Deprecated
        @Override
        public int getPageNumber() {
            return querier.getPageNo();
        }

        @Override
        public int getPageNo() {
            return querier.getPageNo();
        }

        @Override
        public int getPageSize() {
            return querier.getPageSize();
        }

        @Override
        public int getOffset() {
            return querier.getOffset();
        }

        @Override
        public String getKeyword() {
            return querier.getKeyword();
        }

        @Override
        public Map<String, Object> getQ() {
            return querier.getQ();
        }

        @Override
        public String getDistinct() {
            return querier.getDistinct();
        }

        @Override
        public List<? extends Column> getColumns() {
            return querier.getColumns();
        }

        @Override
        public Map<String, Object> getWhere() {
            return querier.getWhere();
        }

        @Override
        public Map<String, String> getSort() {
            return querier.getSort();
        }

        @Override
        public Map<String, String> getAggr() {
            return aggr == null ? Collections.EMPTY_MAP : aggr;
        }
    }
}
