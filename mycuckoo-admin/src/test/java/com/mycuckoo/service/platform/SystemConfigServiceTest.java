package com.mycuckoo.service.platform;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.util.SystemConfigLoader;
import com.mycuckoo.core.xml.SystemConfigXml;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class SystemConfigServiceTest {
    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final String CONFIG_PATH = "config/SystemConfig.xml";

    @Test
    public void shouldAddAdminUsers() {
        SystemConfigService service = new SystemConfigService();
        SystemConfigXml configXml = createConfigXml("admin");
        StringBuilder optContent = new StringBuilder();

        service.applyAdminUsersChange(configXml, Arrays.asList("tester", "auditor"), "add", optContent);

        Assert.assertEquals(Arrays.asList("admin", "tester", "auditor"), configXml.getAdminUsers());
        Assert.assertTrue(optContent.toString().contains("增加管理员:tester,"));
        Assert.assertTrue(optContent.toString().contains("增加管理员:auditor,"));
    }

    @Test
    public void shouldDeleteAdminUsers() {
        SystemConfigService service = new SystemConfigService();
        SystemConfigXml configXml = createConfigXml("admin", "tester", "auditor");
        StringBuilder optContent = new StringBuilder();

        service.applyAdminUsersChange(configXml, Arrays.asList("tester", "missing"), "delete", optContent);

        Assert.assertEquals(Arrays.asList("admin", "auditor"), configXml.getAdminUsers());
        Assert.assertTrue(optContent.toString().contains("删除管理员:tester,"));
        Assert.assertTrue(optContent.toString().contains("删除管理员:missing,"));
    }

    @Test
    public void shouldIgnoreUnknownAdminAction() {
        SystemConfigService service = new SystemConfigService();
        SystemConfigXml configXml = createConfigXml("admin");
        StringBuilder optContent = new StringBuilder();

        service.applyAdminUsersChange(configXml, Arrays.asList("tester"), "noop", optContent);

        Assert.assertEquals(Arrays.asList("admin"), configXml.getAdminUsers());
        Assert.assertEquals("", optContent.toString());
    }

    @Test
    public void shouldSaveSystemConfigToTempFile() throws Exception {
        withLoadedTempConfig("system-config-save-", tempFile -> {
            SystemConfigService service = new SystemConfigService();
            SystemConfigXml config = new SystemConfigXml();
            config.setLogLevel("2");
            service.saveSystemConfig(config, null);

            String xml = new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8);
            Assert.assertTrue(xml.contains("<logLevel>2</logLevel>"));
            Assert.assertTrue(xml.contains("<logRetentionDays>30</logRetentionDays>"));

            SystemConfigXml reloadedConfig = XML_MAPPER.readValue(tempFile, SystemConfigXml.class);
            Assert.assertEquals("2", reloadedConfig.getLogLevel());
        });
    }

    @Test
    public void shouldDeleteAdminUserThroughSaveSystemConfig() throws Exception {
        withLoadedTempConfig("system-config-delete-", tempFile -> {
            SystemConfigService service = new SystemConfigService();

            SystemConfigXml addConfig = new SystemConfigXml();
            addConfig.setAdminUsers(Arrays.asList("tester", "auditor"));
            service.saveSystemConfig(addConfig, "add");

            SystemConfigXml deleteConfig = new SystemConfigXml();
            deleteConfig.setAdminUsers(Arrays.asList("tester"));
            service.saveSystemConfig(deleteConfig, "delete");

            SystemConfigXml savedConfig = XML_MAPPER.readValue(tempFile, SystemConfigXml.class);
            Assert.assertTrue(savedConfig.getAdminUsers().contains("admin"));
            Assert.assertTrue(savedConfig.getAdminUsers().contains("auditor"));
            Assert.assertFalse(savedConfig.getAdminUsers().contains("tester"));
        });
    }

    @Test
    public void shouldUpdateSystemNameThroughSaveSystemConfig() throws Exception {
        withLoadedTempConfig("system-config-name-", tempFile -> {
            SystemConfigService service = new SystemConfigService();
            SystemConfigXml config = new SystemConfigXml();
            config.setSystemName("统一权限平台");
            service.saveSystemConfig(config, null);

            SystemConfigXml savedConfig = XML_MAPPER.readValue(tempFile, SystemConfigXml.class);
            Assert.assertEquals("统一权限平台", savedConfig.getSystemName());
            Assert.assertEquals("1", savedConfig.getLogLevel());
        });
    }

    @Test
    public void shouldUpdateDefaultRowPrivilegeLevelThroughSaveSystemConfig() throws Exception {
        withLoadedTempConfig("system-config-privilege-", tempFile -> {
            SystemConfigService service = new SystemConfigService();
            SystemConfigXml config = new SystemConfigXml();
            config.setDefaultRowPrivilegeLevel("user");
            service.saveSystemConfig(config, null);

            SystemConfigXml savedConfig = XML_MAPPER.readValue(tempFile, SystemConfigXml.class);
            Assert.assertEquals("user", savedConfig.getDefaultRowPrivilegeLevel());
            Assert.assertEquals("系统平台统一用户", savedConfig.getSystemName());
        });
    }

    @Test
    public void shouldUpdateLogRetentionDaysThroughSaveSystemConfig() throws Exception {
        withLoadedTempConfig("system-config-retention-", tempFile -> {
            SystemConfigService service = new SystemConfigService();
            SystemConfigXml config = new SystemConfigXml();
            config.setLogRetentionDays("15");
            service.saveSystemConfig(config, null);

            SystemConfigXml savedConfig = XML_MAPPER.readValue(tempFile, SystemConfigXml.class);
            Assert.assertEquals("15", savedConfig.getLogRetentionDays());
            Assert.assertEquals("系统平台统一用户", savedConfig.getSystemName());
        });
    }

    private SystemConfigXml createConfigXml(String... userCodes) {
        SystemConfigXml configXml = new SystemConfigXml();
        configXml.setAdminUsers(new ArrayList<>(Arrays.asList(userCodes)));
        return configXml;
    }

    private File createTempSystemConfigFile(String prefix) throws Exception {
        File tempFile = File.createTempFile(prefix, ".xml");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), Files.readAllBytes(new File(CONFIG_PATH).toPath()));
        return tempFile;
    }

    private void registerNoOpLogPublisher() {
        LogOperator.setEventMulticaster(new NoOpPublisher());
    }

    private File useTempConfigFile(String prefix) throws Exception {
        File tempFile = createTempSystemConfigFile(prefix);
        invokeLoaderMethod("useConfigFileForTest", new Class<?>[]{File.class}, tempFile);
        return tempFile;
    }

    private void withLoadedTempConfig(String prefix, ThrowingFileConsumer consumer) throws Exception {
        registerNoOpLogPublisher();
        File tempFile = useTempConfigFile(prefix);
        try {
            SystemConfigLoader.getInstance().load();
            consumer.accept(tempFile);
        } finally {
            invokeLoaderMethod("clearTestConfigFile", new Class<?>[0]);
        }
    }

    private void invokeLoaderMethod(String methodName, Class<?>[] parameterTypes, Object... args) throws Exception {
        Method method = SystemConfigLoader.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        method.invoke(SystemConfigLoader.getInstance(), args);
    }

    @FunctionalInterface
    private interface ThrowingFileConsumer {
        void accept(File file) throws Exception;
    }

    private static class NoOpPublisher implements ApplicationEventPublisher {
        @Override
        public void publishEvent(ApplicationEvent event) {
        }

        @Override
        public void publishEvent(Object event) {
        }
    }
}
