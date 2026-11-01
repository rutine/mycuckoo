package com.mycuckoo.admin.service.cms.processor;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class AmountProcessor implements FieldProcessor {

    @Override
    public String name() {
        return "amount";
    }

    @Override
    public Object beforeSave(FieldContext ctx, Object value) {
        if (value == null) return null;
        // 前端传元，入库转分
        return new BigDecimal(value.toString())
                .multiply(new BigDecimal(100))
                .longValue();
    }

    @Override
    public Object afterLoad(FieldContext ctx, Object value) {
        if (value == null) return null;
        // 出库转元
        return new BigDecimal(value.toString())
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public Object forQuery(FieldContext ctx, Object value) {
        if (value == null) return null;
        return beforeSave(ctx, value);
    }
}