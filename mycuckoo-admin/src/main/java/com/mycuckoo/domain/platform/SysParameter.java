package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:06:28 PM
 */
public class SysParameter implements Serializable {

    private Long paraId;
    private String name;
    private String key;
    private String value;
    private String type;
    private String memo;
    private String status;
    private String updater; //更新人
    private Date updateDate; //更新时间
    private String creator;
    private Date createDate;

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

    /**
     * full constructor
     */
    public SysParameter(Long paraId, String name, String key,
                        String value, String type, String memo, String status,
                        String updater, Date updateDate, String creator, Date createDate) {
        this.paraId = paraId;
        this.name = name;
        this.key = key;
        this.value = value;
        this.type = type;
        this.memo = memo;
        this.status = status;
        this.updater = updater;
        this.updateDate = updateDate;
        this.creator = creator;
        this.createDate = createDate;
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
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
