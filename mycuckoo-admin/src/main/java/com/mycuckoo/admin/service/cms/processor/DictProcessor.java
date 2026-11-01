package com.mycuckoo.admin.service.cms.processor;

import com.mycuckoo.admin.service.platform.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DictProcessor implements FieldProcessor {

    @Autowired
    private DictionaryService dictService;


    @Override
    public String name() {
        return "dict";
    }

    @Override
    public Object beforeSave(FieldContext ctx, Object value) {
        // 入库存 code
        return value;
    }

    @Override
    public Object afterLoad(FieldContext ctx, Object value) {
        if (value == null) return null;
        String dictCode = ctx.getField().getExtra();
        return dictService.findItemsByDictCode(dictCode).stream()
                .filter(o -> o.getCode().equals(value))
                .map(o -> o.getName())
                .findFirst().orElse(null);
    }

    @Override
    public Object forQuery(FieldContext ctx, Object value) {
        return value;
    }

}