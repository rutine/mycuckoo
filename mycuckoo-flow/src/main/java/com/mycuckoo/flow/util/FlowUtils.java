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
            vo.setKey(processId);
            vo.setName(processName);
            return vo;
        } catch (MyCuckooException e) {
            throw e;
        } catch (Exception e) {
            throw new MyCuckooException("流程XML解析失败", e);
        }
    }


    /**
     * 流程完成时间处理
     */
    public static String formatDate(Long time) {
        if (time == null || time == 0L) {
            return "";
        }

        long day = time / (24 * 60 * 60 * 1000);
        long hour = (time / (60 * 60 * 1000) - day * 24);
        long minute = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        } else if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        } else if (minute > 0) {
            return minute + "分钟";
        } else if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }
}
