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
    public ModOptRef(Long modOptId, Operate operate,
                     ModuleMenu moduleMemu) {
        this.modOptId = modOptId;
        this.operate = operate;
        this.moduleMemu = moduleMemu;
    }

    public Long getModOptId() {
        return this.modOptId;
    }

    public void setModOptId(Long modOptId) {
        this.modOptId = modOptId;
    }

    public Operate getOperate() {
        return this.operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }

    public ModuleMenu getModuleMemu() {
        return this.moduleMemu;
    }

    public void setModuleMemu(ModuleMenu moduleMemu) {
        this.moduleMemu = moduleMemu;
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
