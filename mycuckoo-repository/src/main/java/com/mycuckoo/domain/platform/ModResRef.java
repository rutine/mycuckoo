package com.mycuckoo.domain.platform;

import java.io.Serializable;

/**
 * 功能说明: 模块资源关系
 *
 * @author rutine
 * @version 4.1.0
 * @time May 12, 2024 11:04:15 AM
 */
public class ModResRef implements Serializable {

    private Long modResId;
    private Resource resource;
    private ModuleMenu moduleMemu;
    private Integer group;
    private Integer order;

    private Long resourceId;

    /**
     * default constructor
     */
    public ModResRef() {
    }

    /**
     * minimal constructor
     */
    public ModResRef(Long modResId) {
        this.modResId = modResId;
    }

    /**
     * full constructor
     */
    public ModResRef(Long modResId, Resource resource, ModuleMenu moduleMemu, Integer group, Integer order) {
        this.modResId = modResId;
        this.resource = resource;
        this.moduleMemu = moduleMemu;
        this.group = group;
        this.order = order;
    }

    public Long getModResId() {
        return modResId;
    }

    public void setModResId(Long modResId) {
        this.modResId = modResId;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ModuleMenu getModuleMemu() {
        return moduleMemu;
    }

    public void setModuleMemu(ModuleMenu moduleMemu) {
        this.moduleMemu = moduleMemu;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        ModResRef modOptRef = (ModResRef) obj;
        if (modOptRef.getModResId() != null && modResId != null &&
                modOptRef.getModResId().longValue() ==
                        this.getModResId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (modResId == null ? 0 : modResId.hashCode());

        return result;
    }
}
