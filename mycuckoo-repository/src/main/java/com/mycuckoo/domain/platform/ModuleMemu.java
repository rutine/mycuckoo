package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:04:59 PM
 */
public class ModuleMemu implements Serializable, Comparator<ModuleMemu> {

    private Long moduleId; //模块ID
    private Long parentId; //上级模块
    private String modName; //模块名称
    private String modEnId; //模块英文ID
    private String modImgCls; //模块图片样式
    private String modLevel; //模块级别
    private Integer modOrder; //模块顺序
    private String modPageType;//页面类型
    private String belongToSys;//所属系统
    private String status;
    private String memo; //备注
    private String creator; //创建人
    private Date createDate; //创建时间

    /**
     * default constructor
     */
    public ModuleMemu() {
    }

    /**
     * minimal constructor
     */
    public ModuleMemu(Long moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * full constructor
     */
    public ModuleMemu(Long moduleId, Long parentId,
                      String modName, String modEnId, String modImgCls, String modLevel,
                      Integer modOrder, String status,
                      String memo, String creator, Date createDate, List<ModuleMemu> moduleMemus) {
        this.moduleId = moduleId;
        this.parentId = parentId;
        this.modName = modName;
        this.modEnId = modEnId;
        this.modImgCls = modImgCls;
        this.modLevel = modLevel;
        this.modOrder = modOrder;
        this.status = status;
        this.memo = memo;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getModName() {
        return this.modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getModEnId() {
        return this.modEnId;
    }

    public void setModEnId(String modEnId) {
        this.modEnId = modEnId;
    }

    public String getModImgCls() {
        return this.modImgCls;
    }

    public void setModImgCls(String modImgCls) {
        this.modImgCls = modImgCls;
    }

    public String getModLevel() {
        return this.modLevel;
    }

    public void setModLevel(String modLevel) {
        this.modLevel = modLevel;
    }

    public Integer getModOrder() {
        return this.modOrder;
    }

    public void setModOrder(Integer modOrder) {
        this.modOrder = modOrder;
    }

    public String getModPageType() {
        return modPageType;
    }

    public void setModPageType(String modPageType) {
        this.modPageType = modPageType;
    }

    public String getBelongToSys() {
        return belongToSys;
    }

    public void setBelongToSys(String belongToSys) {
        this.belongToSys = belongToSys;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
    public int compare(ModuleMemu moduleMemu1, ModuleMemu moduleMemu2) {
        return moduleMemu1.getModuleId().compareTo(moduleMemu2.getModuleId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        ModuleMemu moduleMemu = (ModuleMemu) obj;
        if (moduleId != null && moduleMemu.getModuleId() != null &&
                this.moduleId.longValue() == moduleMemu.getModuleId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (moduleId == null ? 0 : moduleId.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
