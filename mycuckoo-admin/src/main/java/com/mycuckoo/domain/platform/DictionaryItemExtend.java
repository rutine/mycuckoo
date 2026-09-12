package com.mycuckoo.domain.platform;

/**
 * 功能说明: 字典项扩展对象
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 10:00
 */
public class DictionaryItemExtend extends DictionaryItem {
    private String dictCode;

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode == null ? dictCode : dictCode.trim();
    }
}