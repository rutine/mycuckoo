package com.mycuckoo.admin.entity.dto.cms;

import java.util.List;
import java.util.Map;

public abstract class FormDTOs {

    public static class SaveDTO {
        private Long id;
        private String code;
        private String name;
        private String bizType;
        private String memo;
        private List<FieldConfig> configs;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getBizType() { return bizType; }
        public void setBizType(String bizType) { this.bizType = bizType; }

        public String getMemo() { return memo; }
        public void setMemo(String memo) { this.memo = memo; }

        public List<FieldConfig> getConfigs() { return configs; }
        public void setConfigs(List<FieldConfig> configs) { this.configs = configs; }
    }

    public static class DataSaveDTO {
        private Integer id;
        private String formCode;
        private String bizId;
        private Map<String, Object> data;
        // getter / setter 略


        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getFormCode() {
            return formCode;
        }
        public void setFormCode(String formCode) {
            this.formCode = formCode;
        }

        public String getBizId() {
            return bizId;
        }
        public void setBizId(String bizId) {
            this.bizId = bizId;
        }

        public Map<String, Object> getData() {
            return data;
        }
        public void setData(Map<String, Object> data) {
            this.data = data;
        }
    }

    public static class ReprocessDTO {
        private String formCode;
        private String field;
        private String operator;

        public String getFormCode() { return formCode; }
        public void setFormCode(String formCode) { this.formCode = formCode; }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
    }

    public static class UpdateFieldDTO {
        private Long dataId;
        private String field;
        private Object value;
        private String operator;

        public Long getDataId() { return dataId; }
        public void setDataId(Long dataId) { this.dataId = dataId; }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }

        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
    }
}
