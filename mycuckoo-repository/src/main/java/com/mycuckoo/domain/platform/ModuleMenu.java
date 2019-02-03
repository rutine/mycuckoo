package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:04:59 PM
 */
public class ModuleMenu implements Serializable, Comparator<ModuleMenu> {

    private Long moduleId; //模块ID
    private Long parentId; //上级模块
    private String modName; //模块名称
    private String modEnName; //模块英文ID
    private String modIconCls; //模块图片样式
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
    public ModuleMenu() {
    }

    /**
     * minimal constructor
     */
    public ModuleMenu(Long moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * full constructor
     */
    public ModuleMenu(Long moduleId, Long parentId,
                      String modName, String modEnName, String modIconCls, String modLevel,
                      Integer modOrder, String status,
                      String memo, String creator, Date createDate) {
        this.moduleId = moduleId;
        this.parentId = parentId;
        this.modName = modName;
        this.modEnName = modEnName;
        this.modIconCls = modIconCls;
        this.modLevel = modLevel;
        this.modOrder = modOrder;
        this.status = status;
        this.memo = memo;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getModuleId() {
        return moduleId;
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

    public String getModEnName() {
        return modEnName;
    }

    public void setModEnName(String modEnName) {
        this.modEnName = modEnName;
    }

    public String getModIconCls() {
        return modIconCls;
    }

    public void setModIconCls(String modIconCls) {
        this.modIconCls = modIconCls;
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
    public int compare(ModuleMenu moduleMemu1, ModuleMenu moduleMemu2) {
        return moduleMemu1.getModuleId().compareTo(moduleMemu2.getModuleId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        ModuleMenu moduleMemu = (ModuleMenu) obj;
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
