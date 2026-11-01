package com.mycuckoo.admin.entity.vo.cms;

import com.mycuckoo.admin.entity.dto.cms.FieldConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FormVO {
    private Long id;
    private String code;
    private String name;
    private Integer version;
    private Integer status;
    private String memo;
    private List<FieldConfig> configs;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public List<FieldConfig> getConfigs() { return configs; }
    public void setConfigs(List<FieldConfig> configs) { this.configs = configs; }
}