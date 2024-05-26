package com.mycuckoo.core.repository.param;

/**
 * 功能说明: 过滤匹配类型
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:06
 */
public enum FilterType {
    EQ("eq", "等于"),
    LIKE("like", "前缀模糊"),
    TEXT("text", "模糊"),
    SCOPE("scope", "between区间"),
    MULTI("multi", "in范围"),
    PROVINCE_CITY("province_city", "地区"),
    PROVINCE_CITY_AREA("province_city_area", "地区"),
    BOOL("bool", "是否");

    private String code;
    private String desc;

    FilterType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}