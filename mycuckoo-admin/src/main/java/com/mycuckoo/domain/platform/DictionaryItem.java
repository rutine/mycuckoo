package com.mycuckoo.domain.platform;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Comparator;

/**
 * 功能说明: 字典项域对象
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 10:00
 */
public class DictionaryItem extends BasicDomain<Long> implements com.mycuckoo.core.Dictionary {

    private Long itemId;
    private Long dictId;
    private String code;
    private String name;

    /**
     * default constructor
     */
    public DictionaryItem() {
    }

    /**
     * minimal constructor
     */
    public DictionaryItem(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getDictId() {
        return dictId;
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

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        DictionaryItem that = (DictionaryItem) obj;

        if (that.getItemId() != null && this.itemId != null &&
                that.getItemId().longValue() == this.itemId.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (itemId == null ? 0 : itemId.hashCode());
        return result;
    }

    public DictionaryItemComp getItemComp() {
        return new DictionaryItemComp();
    }

    class DictionaryItemComp implements Comparator<DictionaryItem> {
        public int compare(DictionaryItem item1, DictionaryItem item2) {
            return item1.getItemId().compareTo(item2.getItemId());
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}