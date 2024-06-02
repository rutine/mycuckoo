package com.mycuckoo.web.vo.req;

import java.util.Date;
import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/2 6:31
 */
public abstract class TableConfigReqVos {

    public static class Create {
        private Long moduleId;
        private String tableCode;
        private List<CreateConfig> configs;

        public Long getModuleId() {
            return moduleId;
        }

        public void setModuleId(Long moduleId) {
            this.moduleId = moduleId;
        }

        public String getTableCode() {
            return tableCode;
        }

        public void setTableCode(String tableCode) {
            this.tableCode = tableCode;
        }

        public List<CreateConfig> getConfigs() {
            return configs;
        }

        public void setConfigs(List<CreateConfig> configs) {
            this.configs = configs;
        }
    }

    public static class CreateConfig {
        private Long tableId; //ID
        private String type; //类型
        private String field; //字段
        private String title; //标题
        private String filterType; //过滤类型
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
}
