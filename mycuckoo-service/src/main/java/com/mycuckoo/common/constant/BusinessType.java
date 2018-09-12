package com.mycuckoo.common.constant;

/**
 * 功能说明: 文件对应的业务类型
 *
 * @author rutine
 * @version 3.0.0
 * @time Sept 30, 2017 16:06:41 PM
 */
public enum BusinessType {
    photo(0, "photo"),
    document(1, "documents");

    BusinessType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private int code;
    private String name;
}
