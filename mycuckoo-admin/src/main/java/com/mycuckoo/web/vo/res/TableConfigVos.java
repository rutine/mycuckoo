package com.mycuckoo.web.vo.res;

import com.mycuckoo.core.Dictionary;

import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/2 7:28
 */
public abstract class TableConfigVos {

    public static class Detail {
        private Long tableId;
        private String tableCode;
        private String type;
        private String field;
        private String title;
        private String filterType;
        private Boolean filter;
        private Boolean sort;
        private Boolean blank;
        private Integer width;
        private String extra;

        public Long getTableId() {
            return tableId;
        }

        public void setTableId(Long tableId) {
            this.tableId = tableId;
        }

        public String getTableCode() {
            return tableCode;
        }

        public void setTableCode(String tableCode) {
            this.tableCode = tableCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFilterType() {
            return filterType;
        }

        public void setFilterType(String filterType) {
            this.filterType = filterType;
        }

        public Boolean getFilter() {
            return filter;
        }

        public void setFilter(Boolean filter) {
            this.filter = filter;
        }

        public Boolean getSort() {
            return sort;
        }

        public void setSort(Boolean sort) {
            this.sort = sort;
        }

        public Boolean getBlank() {
            return blank;
        }

        public void setBlank(Boolean blank) {
            this.blank = blank;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }
    }

    public static class Config extends Detail {
        private List<? extends Dictionary> dict;

        public List<? extends Dictionary> getDict() {
            return dict;
        }

        public void setDict(List<? extends Dictionary> dict) {
            this.dict = dict;
        }
    }
}
