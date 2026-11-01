package com.mycuckoo.admin.service.cms.processor;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class DateProcessor implements FieldProcessor {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String name() {
        return "date";
    }

    @Override
    public Object beforeSave(FieldContext ctx, Object value) {
        // 入库统一存字符串
        if (value == null) return null;
        return value.toString();
    }

    @Override
    public Object afterLoad(FieldContext ctx, Object value) {
        if (value == null) return null;
        return value.toString();
    }
}