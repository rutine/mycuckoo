package com.mycuckoo.domain.platform;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:03:22 PM
 */
public class DictBigType implements Serializable {

    private Long bigTypeId;
    private String code;
    private String name;
    private String memo;
    private String status;
    private String updater;
    private Date updateDate;
    private String creator;
    private Date createDate;
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

    /**
     * full constructor
     */
    public DictBigType(Long bigTypeId, String code, String name,
                       String memo, String status, String updater, Date updateDate,
                       String creator, Date createDate, List<DictSmallType> smallTypes) {
        this.bigTypeId = bigTypeId;
        this.code = code;
        this.name = name;
        this.memo = memo;
        this.status = status;
        this.updater = updater;
        this.updateDate = updateDate;
        this.creator = creator;
        this.createDate = createDate;
        this.smallTypes = smallTypes;
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
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
