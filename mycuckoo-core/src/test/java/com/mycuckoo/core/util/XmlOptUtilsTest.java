package com.mycuckoo.core.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlOptUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testReadXmlAndSelectTomcatDefaultAttribute() throws Exception {
        Document doc = XmlOptUtils.readXML("config/SystemConfig.xml");
        List<Node> elList = doc.selectNodes("//jmx/tomcat");

        Assert.assertNotNull(elList);
        Assert.assertFalse(elList.isEmpty());
        Assert.assertEquals("true", ((Element) elList.get(0)).attributeValue("active"));
    }
}
