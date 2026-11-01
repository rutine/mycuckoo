package com.mycuckoo.admin.entity.cms;

import java.math.BigDecimal;

public class CmsFormField {

    // ========== 基础属性 ==========
    private Long id;
    private Long formId;
    private String formCode;
    private Integer type;
    private String field;
    private String title;
    private Integer col;
    private Integer required;
    private String processor;
    private String extra;

    // ========== 通用值 ==========
    private String placeholder;
    private String defaultValue;
    private Boolean defaultBool;

    // ========== 校验属性 ==========
    private Integer max;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String regex;

    // ========== 文件/图片 ==========
    private String accept;
    private String exts;

    // ========== 选项 ==========
    private String options;  // JSON 字符串

    // ========== 分组 ==========
    private String groupId;
    private String groupTitle;

    // ========== 排序 ==========
    private Integer sort;

    public CmsFormField() {}

    // ========== getter / setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFormId() { return formId; }
    public void setFormId(Long formId) { this.formId = formId; }

    public String getFormCode() { return formCode; }
    public void setFormCode(String formCode) { this.formCode = formCode; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCol() { return col; }
    public void setCol(Integer col) { this.col = col; }

    public Integer getRequired() { return required; }
    public void setRequired(Integer required) { this.required = required; }

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

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupTitle() { return groupTitle; }
    public void setGroupTitle(String groupTitle) { this.groupTitle = groupTitle; }

    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
}