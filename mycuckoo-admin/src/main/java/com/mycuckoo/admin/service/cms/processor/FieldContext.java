package com.mycuckoo.admin.service.cms.processor;

import com.mycuckoo.admin.entity.cms.Form;
import com.mycuckoo.admin.entity.cms.FormField;

import java.util.Map;

public class FieldContext {
    private FormField field;
    private Form form;
    private Map<String, Object> extra;

    public FieldContext(FormField field, Form form, Map<String, Object> extra) {
        this.field = field;
        this.form = form;
        this.extra = extra;
    }

    public FormField getField() { return field; }
    public void setField(FormField field) { this.field = field; }

    public Form getForm() { return form; }
    public void setForm(Form form) { this.form = form; }

    public Map<String, Object> getExtra() { return extra; }
    public void setExtra(Map<String, Object> extra) { this.extra = extra; }
}