package com.mycuckoo.flow.util;

import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.flow.web.vo.req.WorkflowVos;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/4/19 14:00
 */
public abstract class FlowUtils {
    private FlowUtils() {}


    public static WorkflowVos.CreateDefinitionVo parseBpmnXml(String bpmnXml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(bpmnXml)));
            NodeList processNodes = document.getElementsByTagNameNS("*", "process");
            if (processNodes == null || processNodes.getLength() == 0) {
                throw new MyCuckooException("流程XML缺少process节点");
            }

            Element process = (Element) processNodes.item(0);
            String processId = process.getAttribute("id");
            if (!StringUtils.hasText(processId)) {
                throw new MyCuckooException("流程XML缺少process id");
            }

            String processName = process.getAttribute("name");
            if (!StringUtils.hasText(processName)) {
                processName = processId;
            }

            WorkflowVos.CreateDefinitionVo vo = new WorkflowVos.CreateDefinitionVo();
            vo.setProcessDefinitionKey(processId);
            vo.setName(processName);
            return vo;
        } catch (MyCuckooException e) {
            throw e;
        } catch (Exception e) {
            throw new MyCuckooException("流程XML解析失败", e);
        }
    }

    private static class ParsedWorkflowDefinition {
        private final String processId;
        private final String processName;

        private ParsedWorkflowDefinition(String processId, String processName) {
            this.processId = processId;
            this.processName = processName;
        }

        public String getProcessName() {
            return processName;
        }

        public String getResourceName() {
            return processId + ".bpmn20.xml";
        }
    }

}
