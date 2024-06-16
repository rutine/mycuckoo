package com.mycuckoo.domain.platform;

import com.mycuckoo.core.Dictionary;
import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Comparator;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:03:38 PM
 */
public class DictSmallType extends BasicDomain<Long> implements Dictionary {

    private Long smallTypeId;
    private Long bigTypeId;
    private String code;
    private String name;

    /**
     * default constructor
     */
    public DictSmallType() {
    }

    /**
     * minimal constructor
     */
    public DictSmallType(Long smallTypeId) {
        this.smallTypeId = smallTypeId;
    }

    public Long getSmallTypeId() {
        return this.smallTypeId;
    }

    public void setSmallTypeId(Long smallTypeId) {
        this.smallTypeId = smallTypeId;
    }

    public Long getBigTypeId() {
        return bigTypeId;
    }

    public void setBigTypeId(Long bigTypeId) {
        this.bigTypeId = bigTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        DictSmallType dictSmallType = (DictSmallType) obj;

        if (dictSmallType.getSmallTypeId() != null && this.smallTypeId != null &&
                dictSmallType.getSmallTypeId().longValue() == this.smallTypeId.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (smallTypeId == null ? 0 : smallTypeId.hashCode());
        return result;
    }

    public DicSmallTypeComp getDicSmallTypeInst() {
        return new DicSmallTypeComp();
    }

    class DicSmallTypeComp implements Comparator<DictSmallType> {
        public int compare(DictSmallType dictSmallType1, DictSmallType dictSmallType2) {
            int flag = dictSmallType1.getSmallTypeId().compareTo(dictSmallType2.getSmallTypeId());

            return flag;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
