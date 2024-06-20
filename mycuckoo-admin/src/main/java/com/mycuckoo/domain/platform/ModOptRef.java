package com.mycuckoo.domain.platform;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:04:18 PM
 */
public class ModOptRef implements Serializable {

    private Long modOptId;
    private Long moduleId;
    private Operate operate;
    private ModuleMenu moduleMemu;

    /**
     * default constructor
     */
    public ModOptRef() {
    }

    /**
     * minimal constructor
     */
    public ModOptRef(Long modOptId) {
        this.modOptId = modOptId;
    }

    /**
     * full constructor
     */
    public ModOptRef(Long modOptId, Long moduleId, Operate operate) {
        this.modOptId = modOptId;
        this.moduleId = moduleId;
        this.operate = operate;
    }

    public Long getModOptId() {
        return this.modOptId;
    }

    public void setModOptId(Long modOptId) {
        this.modOptId = modOptId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Operate getOperate() {
        return this.operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        ModOptRef modOptRef = (ModOptRef) obj;
        if (modOptRef.getModOptId() != null && modOptId != null &&
                modOptRef.getModOptId().longValue() ==
                        this.getModOptId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (modOptId == null ? 0 : modOptId.hashCode());

        return result;
    }
}
