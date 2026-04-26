package com.mycuckoo.flow.web.vo.req;

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
}
