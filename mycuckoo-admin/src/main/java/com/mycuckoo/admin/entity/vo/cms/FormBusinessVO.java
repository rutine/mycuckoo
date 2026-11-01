package com.mycuckoo.admin.entity.vo.cms;

import com.mycuckoo.admin.entity.dto.cms.FieldConfig;
import java.time.LocalDateTime;
import java.util.Map;

public class FormBusinessVO {
    private Long formId;
    private String formCode;
    private String formName;
    private Integer formVersion;
    private java.util.List<FieldConfig> configs;
    private Long dataId;
    private String bizId;
    private Map<String, Object> data;
    private String creator;
    private LocalDateTime createTime;
    private String updater;
    private LocalDateTime updateTime;

    // getter / setter
    public Long getFormId() { return formId; }
    public void setFormId(Long formId) { this.formId = formId; }

    public String getFormCode() { return formCode; }
    public void setFormCode(String formCode) { this.formCode = formCode; }

    public String getFormName() { return formName; }
    public void setFormName(String formName) { this.formName = formName; }

    public Integer getFormVersion() { return formVersion; }
    public void setFormVersion(Integer formVersion) { this.formVersion = formVersion; }

    public java.util.List<FieldConfig> getConfigs() { return configs; }
    public void setConfigs(java.util.List<FieldConfig> configs) { this.configs = configs; }

    public Long getDataId() { return dataId; }
    public void setDataId(Long dataId) { this.dataId = dataId; }

    public String getBizId() { return bizId; }
    public void setBizId(String bizId) { this.bizId = bizId; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public String getUpdater() { return updater; }
    public void setUpdater(String updater) { this.updater = updater; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}