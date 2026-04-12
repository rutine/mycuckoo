package com.mycuckoo.core.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class SystemConfigXmlTest {
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    @Test
    public void shouldReadSystemConfigXml() throws Exception {
        File xmlFile = new File("config/SystemConfig.xml");

        SystemConfigXml configXml = XML_MAPPER.readValue(xmlFile, SystemConfigXml.class);

        Assert.assertNotNull(configXml);
        Assert.assertEquals("系统平台统一用户", configXml.getSystemName());
        Assert.assertEquals("1", configXml.getLogLevel());
        Assert.assertEquals("30", configXml.getLogRetentionDays());
        Assert.assertEquals("org", configXml.getDefaultRowPrivilegeLevel());
        Assert.assertNotNull(configXml.getAdminUsers());
        Assert.assertTrue(configXml.getAdminUsers().contains("admin"));
        Assert.assertEquals("true", configXml.getTomcatJmxActive());
        Assert.assertNotNull(configXml.getTomcatJmxUrl());
        Assert.assertNotNull(configXml.getTomcatMbeanName());
    }

    @Test
    public void shouldWriteSystemConfigXml() throws Exception {
        SystemConfigXml configXml = new SystemConfigXml();
        configXml.setSystemName("测试系统");
        configXml.setLogLevel("2");
        configXml.setLogRetentionDays("15");
        configXml.setCluster("false");
        configXml.setDefaultRowPrivilegeLevel("user");

        configXml.setAdminUsers(java.util.Arrays.asList("admin", "tester"));

        configXml.setTomcatJmxActive("true");
        configXml.setTomcatJmxUrl("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
        configXml.setTomcatMbeanName("Catalina:type=Manager,context=/mycuckoo,host=localhost");

        String xml = XML_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(configXml);

        Assert.assertTrue(xml.contains("<logLevel>2</logLevel>"));
        Assert.assertTrue(xml.contains("<logRetentionDays>15</logRetentionDays>"));
        Assert.assertTrue(xml.contains("<adminUsers>"));
        Assert.assertTrue(xml.contains("<userCode>admin</userCode>"));
        Assert.assertTrue(xml.contains("<userCode>tester</userCode>"));
        Assert.assertTrue(xml.contains("<defaultRowPrivilegeLevel>user</defaultRowPrivilegeLevel>"));
        Assert.assertTrue(xml.contains("<jmx>"));
        Assert.assertTrue(xml.contains("active=\"true\""));
        Assert.assertTrue(xml.contains("<jmxUrl>service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi</jmxUrl>"));
        Assert.assertTrue(xml.contains("<mbeanName>Catalina:type=Manager,context=/mycuckoo,host=localhost</mbeanName>"));
    }
}
