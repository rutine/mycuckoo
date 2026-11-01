package com.mycuckoo.admin.service.cms.processor;

import org.springframework.stereotype.Component;

@Component
public class MobileProcessor implements FieldProcessor {

    @Override
    public String name() {
        return "mobile";
    }

    @Override
    public Object beforeSave(FieldContext ctx, Object value) {
        if (value == null) return null;
        // 去掉空格
        return value.toString().replaceAll("\\s", "");
    }

    @Override
    public Object afterLoad(FieldContext ctx, Object value) {
        if (value == null) return null;
        String mobile = value.toString();
        if (mobile.length() == 11) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7);
        }
        return mobile;
    }

    @Override
    public Object forQuery(FieldContext ctx, Object value) {
        // 查询用原值
        return value;
    }
}