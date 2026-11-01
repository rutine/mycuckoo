package com.mycuckoo.admin.service.cms.processor;

import com.mycuckoo.core.util.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MultiProcessor implements FieldProcessor {

    @Override
    public String name() {
        return "multi";
    }

    @Override
    public Object beforeSave(FieldContext ctx, Object value) {
        if (value == null) return null;
        if (value instanceof List) {
            return JsonUtils.toJson(value);
        }
        return value;
    }

    @Override
    public Object afterLoad(FieldContext ctx, Object value) {
        if (value == null) return null;
        if (value instanceof String) {
            return JsonUtils.fromJson((String) value, Object.class);
        }
        return value;
    }

    @Override
    public Object forQuery(FieldContext ctx, Object value) {
        if (value == null) return null;
        return JsonUtils.toJson(Arrays.asList(value));
    }
}