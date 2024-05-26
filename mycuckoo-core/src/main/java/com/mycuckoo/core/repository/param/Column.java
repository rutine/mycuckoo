package com.mycuckoo.core.repository.param;

/**
 * 功能说明: 表头列属性定义
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 9:59
 */
public interface Column {
    /**
     * 字段类型
     */
    String getType();

    /**
     * 字段
     */
    String getField();

    /**
     * 字段名称
     */
    String getName();

    /**
     * 查询类型
     */
    String getFilterType();

    /**
     * 当前字段是否可用于条件过滤
     */
    Boolean isFilter();

    /**
     * 当前字段过滤时是否包含空值
     */
    Boolean isBlank();

    /**
     * 当前字段是否可用于排序
     */
    Boolean isSort();

    /**
     * 额外补充信息
     */
    String getExtra();
}
