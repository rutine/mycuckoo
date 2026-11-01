package com.mycuckoo.admin.entity.cms;

import com.mycuckoo.admin.entity.BasicEntity;

import java.time.LocalDateTime;

public class CmsFormVersion extends BasicEntity<Long> {
    private Long id;
    private Long formId;
    private Integer version;
    private String config;
    private String creator;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFormId() { return formId; }
    public void setFormId(Long formId) { this.formId = formId; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}