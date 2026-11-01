package com.mycuckoo.admin.service.cms.processor;

/**
 * 字段值处理器
 */
public interface FieldProcessor {

    /**
     * 处理器标识，对应字段配置里的 processor
     */
    String name();

    /**
     * 入库前处理：前端提交值 → 存储值
     */
    Object beforeSave(FieldContext ctx, Object value);

    /**
     * 出库后处理：存储值 → 前端显示值
     */
    Object afterLoad(FieldContext ctx, Object value);

    /**
     * 查询条件处理：前端查询值 → SQL 条件值
     */
    default Object forQuery(FieldContext ctx, Object value) {
        return value;
    }
}