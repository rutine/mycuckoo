package com.mycuckoo.admin.entity.dto.cms;

import java.math.BigDecimal;
import java.util.List;

public class FieldConfig {

    // ========== 基础属性 ==========
    private String id;
    private Integer type;
    private String field;
    private String title;
    private Integer col;
    private Boolean required;
    private String processor;
    private String extra;

    // ========== 通用值 ==========
    private String placeholder;
    private String defaultValue;
    private Boolean defaultBool;  // 开关默认开 / 多选 / 半星

    // ========== 校验属性 ==========
    private Integer max;          // 最大长度 / 文件大小 / 评分最大分值
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String regex;

    // ========== 文件/图片 ==========
    private String accept;
    private String exts;

    // ========== 选项/级联 ==========
    private List<Option> options;
    private List<CascaderNode> cascaderData;

    // ========== 分组 ==========
    private List<FieldConfig> children;

    public FieldConfig() {}

    // ========== getter / setter ==========

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCol() { return col; }
    public void setCol(Integer col) { this.col = col; }

    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }

    public String getProcessor() { return processor; }
    public void setProcessor(String processor) { this.processor = processor; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }

    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }

    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }

    public Boolean getDefaultBool() { return defaultBool; }
    public void setDefaultBool(Boolean defaultBool) { this.defaultBool = defaultBool; }

    public Integer getMax() { return max; }
    public void setMax(Integer max) { this.max = max; }

    public BigDecimal getMinValue() { return minValue; }
    public void setMinValue(BigDecimal minValue) { this.minValue = minValue; }

    public BigDecimal getMaxValue() { return maxValue; }
    public void setMaxValue(BigDecimal maxValue) { this.maxValue = maxValue; }

    public String getRegex() { return regex; }
    public void setRegex(String regex) { this.regex = regex; }

    public String getAccept() { return accept; }
    public void setAccept(String accept) { this.accept = accept; }

    public String getExts() { return exts; }
    public void setExts(String exts) { this.exts = exts; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }

    public List<CascaderNode> getCascaderData() { return cascaderData; }
    public void setCascaderData(List<CascaderNode> cascaderData) { this.cascaderData = cascaderData; }

    public List<FieldConfig> getChildren() { return children; }
    public void setChildren(List<FieldConfig> children) { this.children = children; }
}