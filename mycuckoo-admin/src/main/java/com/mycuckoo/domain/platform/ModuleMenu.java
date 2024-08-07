package com.mycuckoo.domain.platform;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Comparator;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:04:59 PM
 */
public class ModuleMenu extends BasicDomain<String> implements Comparator<ModuleMenu> {

    private Long moduleId; //模块ID
    private Long parentId; //上级模块
    private String code; //模块编码
    private String name; //模块名称
    private String iconCls; //模块图片样式
    private Integer level; //模块级别
    private Integer order; //模块顺序
    private String belongSys;//系统归属
    private String pageType;//页面类型
    private String status;
    private String memo; //备注

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
        this.id = String.valueOf(moduleId);
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

    public String getCode() {
        return this.code;
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

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls == null ? iconCls : iconCls.trim();
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getBelongSys() {
        return belongSys;
    }

    public void setBelongSys(String belongSys) {
        this.belongSys = belongSys == null ? belongSys : belongSys.trim();
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType == null ? pageType : pageType.trim();
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status == null ? status : status.trim();
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? memo : memo.trim();
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
