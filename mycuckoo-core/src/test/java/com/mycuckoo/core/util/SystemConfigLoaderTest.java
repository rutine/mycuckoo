package com.mycuckoo.core.util;

import com.mycuckoo.core.xml.SystemConfigXml;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class SystemConfigLoaderTest {
    private static final String SYSTEM_CONFIG_PATH = "config/SystemConfig.xml";

    @Test
    public void shouldLoadSystemConfig() {
        SystemConfigLoader systemConfigLoader = SystemConfigLoader.getInstance();
        systemConfigLoader.load();

        SystemConfigXml config = systemConfigLoader.getConfig();
        Assert.assertNotNull(config);
        Assert.assertEquals("系统平台统一用户", config.getSystemName());
        Assert.assertEquals("1", config.getLogLevel());
        Assert.assertEquals("30", config.getLogRetentionDays());
        Assert.assertEquals("org", config.getDefaultRowPrivilegeLevel());
        Assert.assertNotNull(config.getAdminUsers());
        Assert.assertTrue(config.getAdminUsers().contains("admin"));
        Assert.assertEquals("tomcat", config.getDefaultJmxServer());
        Assert.assertNotNull(config.getTomcatJmxUrl());
        Assert.assertNotNull(config.getTomcatMbeanName());
    }

    @Test
    public void shouldSkipReloadWhenFileNotModified() throws Exception {
        File tempFile = createTempSystemConfigFile("system-config-loader-");

        SystemConfigLoader systemConfigLoader = SystemConfigLoader.getInstance();
        systemConfigLoader.loadConfigFromFile(tempFile);
        SystemConfigXml firstConfig = systemConfigLoader.getConfig();

        Files.writeString(tempFile.toPath(),
                Files.readString(tempFile.toPath(), StandardCharsets.UTF_8).replace("<logLevel>1</logLevel>", "<logLevel>2</logLevel>"),
                StandardCharsets.UTF_8);
        tempFile.setLastModified(1L);

        systemConfigLoader.loadConfigFromFile(tempFile);
        SystemConfigXml secondConfig = systemConfigLoader.getConfig();

        Assert.assertSame(firstConfig, secondConfig);
        Assert.assertEquals("1", secondConfig.getLogLevel());
    }

    @Test
    public void shouldReloadWhenFileModified() throws Exception {
        File tempFile = createTempSystemConfigFile("system-config-loader-reload-");

        SystemConfigLoader systemConfigLoader = SystemConfigLoader.getInstance();
        systemConfigLoader.loadConfigFromFile(tempFile);

        String xml = Files.readString(tempFile.toPath(), StandardCharsets.UTF_8)
                .replace("<logLevel>1</logLevel>", "<logLevel>2</logLevel>");
        Files.writeString(tempFile.toPath(), xml, StandardCharsets.UTF_8);
        tempFile.setLastModified(System.currentTimeMillis() + 2000);

        systemConfigLoader.loadConfigFromFile(tempFile);
        SystemConfigXml reloadedConfig = systemConfigLoader.getConfig();

        Assert.assertEquals("2", reloadedConfig.getLogLevel());
    }

    private File createTempSystemConfigFile(String prefix) throws Exception {
        File tempFile = File.createTempFile(prefix, ".xml");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), Files.readAllBytes(new File(SYSTEM_CONFIG_PATH).toPath()));
        return tempFile;
    }
}
