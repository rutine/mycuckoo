package com.mycuckoo.core.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.xml.SystemConfigXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

/**
 * 功能说明: 系统配置加载器
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:31:33 PM
 */
public class SystemConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfigLoader.class);
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    private static final String CONFIG_FILE = "SystemConfig.xml";
    private static final SystemConfigLoader INSTANCE = new SystemConfigLoader();
    private static final ClassPathResource CONFIG_RESOURCE = new ClassPathResource(CONFIG_FILE);

    private long fileLastModifyTime = 0;
    private final String configFile = CONFIG_FILE;
    private SystemConfigXml config;
    private File configFileForTest;

    private SystemConfigLoader() {
    }

    public static SystemConfigLoader getInstance() {
        if (INSTANCE.config == null) {
            synchronized (SystemConfigLoader.class) {
                if (INSTANCE.config == null) {
                    INSTANCE.load();
                }
            }
        }
        return INSTANCE;
    }

    public void load() {
        try {
            File configFile = resolveConfigFile();
            logger.info("系统配置文件 ---> {}", configFile.getAbsolutePath());

            long configMod = configFile.lastModified();
            if (configMod <= fileLastModifyTime) {
                return;
            }

            config = XML_MAPPER.readValue(configFile, SystemConfigXml.class);
            fileLastModifyTime = configMod;

            logger.info("SystemConfig.xml reload success !");
        } catch (IOException e) {
            logger.info("加载系统配置文件错误", e);
        }
    }

    public void write(SystemConfigXml config) throws SystemException {
        try {
            File configFile = resolveConfigFile();
            XML_MAPPER.writerWithDefaultPrettyPrinter().writeValue(configFile, config);
        } catch (IOException e) {
            throw new SystemException("写入系统配置失败", e);
        }
    }

    public SystemConfigXml getConfig() {
        return config;
    }

    // Test-only hook for loading from a specific file without touching the default path lookup.
    void loadConfigFromFile(File systemConfigFile) throws IOException {
        long configMod = systemConfigFile.lastModified();
        if (configMod <= fileLastModifyTime) {
            return;
        }

        config = XML_MAPPER.readValue(systemConfigFile, SystemConfigXml.class);
        fileLastModifyTime = configMod;
    }

    // Test-only hook for overriding the default config file path.
    void useConfigFileForTest(File configFileForTest) {
        this.configFileForTest = configFileForTest;
        this.fileLastModifyTime = 0;
        this.config = null;
    }

    // Test-only hook for clearing any temporary override state.
    void clearTestConfigFile() {
        this.configFileForTest = null;
        this.fileLastModifyTime = 0;
        this.config = null;
    }

    private File resolveConfigFile() {
        if (configFileForTest != null) {
            return configFileForTest;
        }
        File configFile = new File(FileUtils.getResourcePath(this.configFile));
        if (!configFile.exists()) {
            try {
                FileCopyUtils.copy(CONFIG_RESOURCE.getFile(), configFile);
            } catch (IOException e) {
                logger.info("加载class系统配置文件错误", e);
            }
        }
        if (!configFile.exists()) {
            throw new MyCuckooException("对不起，文件" + configFile.getAbsolutePath() + "找不到.");
        }
        return configFile;
    }
}
