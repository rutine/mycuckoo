package com.mycuckoo.domain.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:03:22 PM
 */
public class DictBigType extends BasicDomain<Long> {

    private Long bigTypeId;
    private String code;
    private String name;
    private String memo;
    private String status;
    private List<DictSmallType> smallTypes = Lists.newArrayList();

    /**
     * default constructor
     */
    public DictBigType() {
    }

    /**
     * minimal constructor
     */
    public DictBigType(Long bigTypeId, String status) {
        this.bigTypeId = bigTypeId;
        this.status = status;
    }

    public Long getBigTypeId() {
        return this.bigTypeId;
    }

    public void setBigTypeId(Long bigTypeId) {
        this.bigTypeId = bigTypeId;
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

    public List<DictSmallType> getSmallTypes() {
        return smallTypes;
    }

    public void setSmallTypes(List<DictSmallType> smallTypes) {
        this.smallTypes = smallTypes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
