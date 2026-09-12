package com.mycuckoo.domain.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 功能说明: 字典域对象
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 10:00
 */
public class Dictionary extends BasicDomain<Long> {

    private Long dictId;
    private String code;
    private String name;
    private String memo;
    private String status;
    private List<DictionaryItem> items = Lists.newArrayList();

    /**
     * default constructor
     */
    public Dictionary() {
    }

    /**
     * minimal constructor
     */
    public Dictionary(Long dictId, String status) {
        this.dictId = dictId;
        this.status = status;
    }

    public Long getDictId() {
        return this.dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? code : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? name : name.trim();
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? memo : memo.trim();
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status == null ? status : status.trim();
    }

    public List<DictionaryItem> getItems() {
        return items;
    }

    public void setItems(List<DictionaryItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}