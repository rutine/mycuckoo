package com.mycuckoo.constant.enums;

/**
 * 功能说明: 文件对应的业务类型
 *
 * @author rutine
 * @version 3.0.0
 * @time Sept 30, 2017 16:06:41 PM
 */
public enum AttachmentType implements com.mycuckoo.core.operator.AttachmentType {
    PHOTO(1, 0, "photo"),
    AFFICHE(2, 0, "affiche");

    AttachmentType(int code, int subCode, String name) {
        this.code = code;
        this.subCode = subCode;
        this.name = name;
    }

    public final int code;
    public final int subCode;
    public final String name;

    @Override
    public int getBusiType() {
        return code;
    }

    @Override
    public int getBusiSubType() {
        return subCode;
    }
}
