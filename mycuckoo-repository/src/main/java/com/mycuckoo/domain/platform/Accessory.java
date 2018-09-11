package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 8:58:08 PM
 */
public class Accessory implements Serializable {

    private Long accessoryId;
    private String accessoryName;
    private Long infoId;

    /**
     * default constructor
     */
    public Accessory() {
    }

    /**
     * minimal constructor
     */
    public Accessory(Long accessoryId) {
        this.accessoryId = accessoryId;
    }

    /**
     * full constructor
     */
    public Accessory(Long accessoryId, String accessoryName, Long infoId) {
        this.accessoryId = accessoryId;
        this.accessoryName = accessoryName;
        this.infoId = infoId;
    }

    public Long getAccessoryId() {
        return this.accessoryId;
    }

    public void setAccessoryId(Long accessoryId) {
        this.accessoryId = accessoryId;
    }

    public String getAccessoryName() {
        return this.accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }

    public Long getInfoId() {
        return this.infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
