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
 * @time Sep 22, 2014 9:04:00 PM
 */
public class District implements Serializable {
    private static final long serialVersionUID = 1000000L;

    private Long districtId;
    private Long parentId;
    private String code;
    private String name;
    private String postal;
    private String telcode;
    private String level;
    private String memo;
    private String status;
    private String updater; //更新人
    private Date updateDate; //更新时间
    private String creator;
    private Date createDate;

    /**
     * default constructor
     */
    public District() {
    }

    /**
     * minimal constructor
     */
    public District(Long districtId, String status) {
        this.districtId = districtId;
        this.status = status;
    }

    /**
     * full constructor
     */
    public District(Long districtId, Long parentId,
                    String code, String name, String postal,
                    String telcode, String level, String memo,
                    String status, String updater, Date updateDate,
                    String creator, Date createDate) {
        this.districtId = districtId;
        this.parentId = parentId;
        this.code = code;
        this.name = name;
        this.postal = postal;
        this.telcode = telcode;
        this.level = level;
        this.memo = memo;
        this.status = status;
        this.updater = updater;
        this.updateDate = updateDate;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getDistrictId() {
        return this.districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getTelcode() {
        return telcode;
    }

    public void setTelcode(String telcode) {
        this.telcode = telcode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        District district = (District) obj;
        if (district.getDistrictId() != null && getDistrictId() != null &&
                district.getDistrictId().longValue() ==
                        this.getDistrictId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getDistrictId() == null ? 0 : getDistrictId().hashCode());

        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
