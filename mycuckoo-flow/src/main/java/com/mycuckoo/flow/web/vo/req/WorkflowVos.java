package com.mycuckoo.flow.web.vo.req;

/**
 * 流程XML部署请求
 *
 * @author rutine
 * @date 2026/4/19 12:00
 */
public abstract class WorkflowVos {

    public static class CreateDefinitionVo {
        private String processDefinitionKey;
        private String name;
        private String description;
        private String xml;

        public String getProcessDefinitionKey() {
            return processDefinitionKey;
        }

        public void setProcessDefinitionKey(String processDefinitionKey) {
            this.processDefinitionKey = processDefinitionKey;
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
}
