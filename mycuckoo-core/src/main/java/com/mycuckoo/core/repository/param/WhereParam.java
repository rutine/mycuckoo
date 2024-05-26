package com.mycuckoo.core.repository.param;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 功能说明: 条件过滤参数
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:28
 */
public class WhereParam extends ColumnAdapter {
    private Object value;
    private boolean isString;
    private boolean isMap;
    private boolean isList;

    public WhereParam(Object value, boolean useNull) {
        this(null, value);
    }

    public WhereParam(Column column, Object value) {
        super(column);

        if (Objects.nonNull(value)) {
            if (value instanceof String) {
                this.isString = true;
            } else if (value instanceof Map) {
                this.isMap = true;
            } else if (value instanceof List) {
                this.isList = true;
            }
            this.value = value;
        } else {
            this.isString = false;
            this.isMap = false;
            this.isList = false;
            this.value = null;//如果value内容为空，则直接置为null，简化逻辑
        }
    }

    public Object getValue() {
        return value;
    }

    public String getText() {
        if (isString) {
            return (String) value;
        }
        return null;
    }

    public List<Object> getList() {
        if (isList) {
            return (List<Object>) value;
        }
        return null;
    }

    public String getProvince() {
        if (isMap) {
            return (String) ((Map) value).get("province");
        }
        return null;
    }

    public String getCity() {
        if (isMap) {
            return (String) ((Map) value).get("city");
        }
        return null;
    }

    public String getArea() {
        if (isMap) {
            return (String) ((Map) value).get("area");
        }
        return null;
    }

    public <T> T getStart() {
        if (isMap) {
            return (T) ((Map) value).get("start");
        }
        return null;
    }

    public <T> T getEnd() {
        if (isMap) {
            return (T) ((Map) value).get("end");
        }
        return null;
    }
//
//    public Map<String, Object> toMap() {
//        Map<String, Object> map = new HashMap<>();
//        map.put(QueryConfigContext.VALUE, this.value);
//        if (useNull) {
//            map.put(QueryConfigContext.WITH_BANK, useNull);
//        }
//
//        return map;
//    }
}
