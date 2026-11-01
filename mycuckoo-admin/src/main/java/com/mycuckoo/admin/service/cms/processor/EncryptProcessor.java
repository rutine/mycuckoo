package com.mycuckoo.admin.service.cms.processor;

import com.mycuckoo.core.util.EncryptUtils;
import org.springframework.stereotype.Component;

@Component
public class EncryptProcessor implements FieldProcessor {

    @Override
    public String name() {
        return "encrypt";
    }

    @Override
    public Object beforeSave(FieldContext ctx, Object value) {
        if (value == null) return null;
        return EncryptUtils.encrypt(value.toString());
    }

    @Override
    public Object afterLoad(FieldContext ctx, Object value) {
        if (value == null) return null;
        return EncryptUtils.decrypt(value.toString());
    }
}