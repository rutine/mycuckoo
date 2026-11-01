package com.mycuckoo.admin.service.cms.processor;

import com.mycuckoo.admin.entity.cms.CmsFormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FieldProcessorFactory {
    private Map<String, FieldProcessor> processors;


    @Autowired
    public void setProcessors(List<FieldProcessor> list) {
        processors = new HashMap<>();
        for (FieldProcessor p : list) {
            processors.put(p.name(), p);
        }
    }


    public FieldProcessor get(String name) {
        if (name == null || name.isEmpty()) return null;
        return processors.get(name);
    }

    public Map<String, Object> beforeSave(List<CmsFormField> fields, Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>(data);
        for (CmsFormField f : fields) {
            if (f.getProcessor() == null || f.getProcessor().isEmpty()) continue;
            FieldProcessor p = get(f.getProcessor());
            if (p == null) continue;
            Object value = result.get(f.getField());
            result.put(f.getField(), p.beforeSave(new FieldContext(f, null, null), value));
        }
        return result;
    }

    public Map<String, Object> afterLoad(List<CmsFormField> fields, Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>(data);
        for (CmsFormField f : fields) {
            if (f.getProcessor() == null || f.getProcessor().isEmpty()) continue;
            FieldProcessor p = get(f.getProcessor());
            if (p == null) continue;
            Object value = result.get(f.getField());
            result.put(f.getField(), p.afterLoad(new FieldContext(f, null, null), value));
        }
        return result;
    }
}