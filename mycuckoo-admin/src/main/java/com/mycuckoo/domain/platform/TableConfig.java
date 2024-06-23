package com.mycuckoo.domain.platform;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/1 10:12
 */
public class TableConfig extends BasicDomain<Long> {
    private Long tableId; //ID
    private String tableCode; //表头编码
    private Long moduleId; //模块ID
    private String type; //类型
    private String field; //字段
    private String title; //标题
    private String filterType; //过滤类型
    private Boolean filter;
    private Boolean sort;
    private Boolean blank;
    private Integer width;
    private String extra;
    private Integer order; //顺序

    /**
     * default constructor
     */
    public TableConfig() {
    }

    /**
     * minimal constructor
     */
    public TableConfig(Long tableId) {
        this.tableId = tableId;
    }

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
        this.tableCode = tableCode == null ? tableCode : tableCode.trim();
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? type : type.trim();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field == null ? field : field.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? title : title.trim();
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType == null ? filterType : filterType.trim();
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
        this.extra = extra == null ? extra : extra.trim();
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        TableConfig other = (TableConfig) obj;
        if ((tableId != null && other.getTableId() != null
                && this.tableId.longValue() == other.getTableId().longValue())
                || (tableCode != null && other.getTableCode() != null
                && this.tableCode.equals(other.getTableCode())
                && field != null && other.getField() != null
                && this.field.equals(other.getField()))) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (tableId == null ? 0 : tableId.hashCode());
        result = 37 * result + (tableCode == null ? 0 : tableCode.hashCode());
        result = 37 * result + (field == null ? 0 : field.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
