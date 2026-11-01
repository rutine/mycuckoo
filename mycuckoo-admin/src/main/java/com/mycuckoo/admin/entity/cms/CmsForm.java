package com.mycuckoo.admin.entity.cms;

import com.mycuckoo.admin.entity.BasicEntity;

import java.time.LocalDateTime;

public class CmsForm extends BasicEntity<Long> {
    private Long id;
    private String code;
    private String name;
    private String bizType;
    private String config;
    private Integer version;
    private Integer status;
    private String memo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }

    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}