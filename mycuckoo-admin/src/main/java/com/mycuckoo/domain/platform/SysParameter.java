package com.mycuckoo.domain.platform;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:06:28 PM
 */
public class SysParameter extends BasicDomain<Long> {

    private Long paraId;
    private String name;
    private String key;
    private String value;
    private String type;
    private String memo;
    private String status;

    /**
     * default constructor
     */
    public SysParameter() {
    }

    /**
     * minimal constructor
     */
    public SysParameter(Long paraId, String status) {
        this.paraId = paraId;
        this.status = status;
    }


    public Long getParaId() {
        return this.paraId;
    }

    public void setParaId(Long paraId) {
        this.paraId = paraId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? name : name.trim();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? key : key.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? value : value.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? type : type.trim();
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


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
