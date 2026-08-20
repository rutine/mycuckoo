package com.mycuckoo.flow.web.vo.req;

import com.mycuckoo.flow.constant.enums.CommentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程XML部署请求
 *
 * @author rutine
 * @date 2026/4/19 12:00
 */
public abstract class WorkflowVos {

    public static class CreateDefinitionVo {
        private String key;
        private String name;
        private String description;
        @NotBlank(message = "流程XML不能为空")
        private String xml;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getXml() {
            return xml;
        }

        public void setXml(String xml) {
            this.xml = xml;
        }
    }

    public static class CreateInstanceVo {
        @NotBlank(message = "流程定义ID不能为空")
        private String processDefinitionId;
        private String formId;
        private String formType;
        private Map<String, Object> formVariables = new HashMap<>();

        public String getProcessDefinitionId() {
            return processDefinitionId;
        }

        public void setProcessDefinitionId(String processDefinitionId) {
            this.processDefinitionId = processDefinitionId;
        }

        public String getFormId() {
            return formId;
        }

        public void setFormId(String formId) {
            this.formId = formId;
        }

        public String getFormType() {
            return formType;
        }

        public void setFormType(String formType) {
            this.formType = formType;
        }

        public Map<String, Object> getFormVariables() {
            return formVariables;
        }

        public void setFormVariables(Map<String, Object> formVariables) {
            this.formVariables = formVariables;
        }
    }

    public static class CompleteTaskVo {
        @NotBlank(message = "待办任务id不能为空")
        private String taskId;
        private CommentType type;
        @NotBlank(message = "审批意见不能为空")
        @Size(message = "审批意见长度不能超过100个字符")
        private String comment;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public CommentType getType() {
            return type;
        }

        public void setType(CommentType type) {
            this.type = type;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
