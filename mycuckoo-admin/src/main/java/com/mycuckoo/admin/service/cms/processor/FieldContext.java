package com.mycuckoo.admin.service.cms.processor;

import com.mycuckoo.admin.entity.cms.CmsForm;
import com.mycuckoo.admin.entity.cms.CmsFormField;

import java.util.Map;

public class FieldContext {
    private CmsFormField field;
    private CmsForm form;
    private Map<String, Object> extra;

    public FieldContext(CmsFormField field, CmsForm form, Map<String, Object> extra) {
        this.field = field;
        this.form = form;
        this.extra = extra;
    }

    public CmsFormField getField() { return field; }
    public void setField(CmsFormField field) { this.field = field; }

    public CmsForm getForm() { return form; }
    public void setForm(CmsForm form) { this.form = form; }

    public Map<String, Object> getExtra() { return extra; }
    public void setExtra(Map<String, Object> extra) { this.extra = extra; }
}