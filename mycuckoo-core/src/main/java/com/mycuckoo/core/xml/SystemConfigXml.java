package com.mycuckoo.core.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能说明: SystemConfig.xml 绑定对象
 *
 * @author rutine
 * @version 5.0.0
 */
@JacksonXmlRootElement(localName = "systemConfig")
public class SystemConfigXml {
    @JacksonXmlProperty(localName = "logLevel")
    private String logLevel;

    @JacksonXmlProperty(localName = "logRetentionDays")
    private String logRetentionDays;

    private String systemName;
    private String cluster;

    @JacksonXmlProperty(localName = "defaultRowPrivilegeLevel")
    private String defaultRowPrivilegeLevel;

    @JacksonXmlProperty(localName = "adminUsers")
    private AdminUsersXml adminUsersNode;

    @JacksonXmlProperty(localName = "jmx")
    private JmxXml jmxNode;

    private String userAddDelFlag;

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogRetentionDays() {
        return logRetentionDays;
    }

    public void setLogRetentionDays(String logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getDefaultRowPrivilegeLevel() {
        return defaultRowPrivilegeLevel;
    }

    public void setDefaultRowPrivilegeLevel(String defaultRowPrivilegeLevel) {
        this.defaultRowPrivilegeLevel = defaultRowPrivilegeLevel;
    }

    @JsonProperty("adminUsers")
    public List<String> getAdminUsers() {
        if (adminUsersNode == null || adminUsersNode.getUserCodes() == null) {
            return Collections.emptyList();
        }
        return adminUsersNode.getUserCodes();
    }

    @JsonProperty("adminUsers")
    public void setAdminUsers(List<String> adminUsers) {
        if (adminUsers == null) {
            this.adminUsersNode = null;
            return;
        }
        if (this.adminUsersNode == null) {
            this.adminUsersNode = new AdminUsersXml();
        }
        this.adminUsersNode.setUserCodes(adminUsers);
    }

    public void addAdminUsers(List<String> userCodes) {
        if (userCodes == null || userCodes.isEmpty()) {
            return;
        }
        Set<String> mergedUserCodes = new LinkedHashSet<>(getAdminUsers());
        mergedUserCodes.addAll(userCodes);
        setAdminUsers(List.copyOf(mergedUserCodes));
    }

    public void removeAdminUsers(List<String> userCodes) {
        if (userCodes == null || userCodes.isEmpty()) {
            return;
        }
        Set<String> remainingUserCodes = new LinkedHashSet<>(getAdminUsers());
        remainingUserCodes.removeAll(userCodes);
        setAdminUsers(List.copyOf(remainingUserCodes));
    }

    @JsonProperty("tomcatJmxUrl")
    public String getTomcatJmxUrl() {
        return jmxNode == null || jmxNode.getTomcat() == null ? null : jmxNode.getTomcat().getJmxUrl();
    }

    @JsonProperty("tomcatJmxUrl")
    public void setTomcatJmxUrl(String tomcatJmxUrl) {
        ensureTomcatNode().setJmxUrl(tomcatJmxUrl);
    }

    @JsonProperty("tomcatMbeanName")
    public String getTomcatMbeanName() {
        return jmxNode == null || jmxNode.getTomcat() == null ? null : jmxNode.getTomcat().getMbeanName();
    }

    @JsonProperty("tomcatMbeanName")
    public void setTomcatMbeanName(String tomcatMbeanName) {
        ensureTomcatNode().setMbeanName(tomcatMbeanName);
    }

    @JsonProperty("defaultJmxServer")
    public String getDefaultJmxServer() {
        if (isActive(jmxNode == null ? null : jmxNode.getTomcat())) {
            return "tomcat";
        }
        if (isActive(jmxNode == null ? null : jmxNode.getWeblogic())) {
            return "weblogic";
        }
        if (isActive(jmxNode == null ? null : jmxNode.getWebsphere())) {
            return "websphere";
        }
        return null;
    }

    @JsonProperty("defaultJmxServer")
    public void setDefaultJmxServer(String defaultJmxServer) {
        ensureJmxNode();
        applyDefaultServer(defaultJmxServer, "tomcat", jmxNode.getTomcat());
        applyDefaultServer(defaultJmxServer, "weblogic", jmxNode.getWeblogic());
        applyDefaultServer(defaultJmxServer, "websphere", jmxNode.getWebsphere());
    }

    public String getUserAddDelFlag() {
        return userAddDelFlag;
    }

    public void setUserAddDelFlag(String userAddDelFlag) {
        this.userAddDelFlag = userAddDelFlag;
    }

    @JsonIgnore
    public String getTomcatJmxActive() {
        return jmxNode == null || jmxNode.getTomcat() == null ? null : jmxNode.getTomcat().getActive();
    }

    @JsonIgnore
    public void setTomcatJmxActive(String active) {
        ensureTomcatNode().setActive(active);
    }

    /**
     * 功能说明: 系统管理员配置节点
     */
    public static class AdminUsersXml {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "userCode")
        private List<String> userCodes;

        public List<String> getUserCodes() {
            return userCodes;
        }

        public void setUserCodes(List<String> userCodes) {
            this.userCodes = userCodes;
        }
    }

    /**
     * 功能说明: JMX 配置节点
     */
    public static class JmxXml {
        private JmxServerXml tomcat;
        private JmxServerXml weblogic;
        private JmxServerXml websphere;

        public JmxServerXml getTomcat() {
            return tomcat;
        }

        public void setTomcat(JmxServerXml tomcat) {
            this.tomcat = tomcat;
        }

        public JmxServerXml getWeblogic() {
            return weblogic;
        }

        public void setWeblogic(JmxServerXml weblogic) {
            this.weblogic = weblogic;
        }

        public JmxServerXml getWebsphere() {
            return websphere;
        }

        public void setWebsphere(JmxServerXml websphere) {
            this.websphere = websphere;
        }
    }

    /**
     * 功能说明: 单个应用服务器 JMX 节点
     */
    public static class JmxServerXml {
        @JacksonXmlProperty(isAttribute = true, localName = "active")
        private String active;

        @JacksonXmlProperty(localName = "jmxUrl")
        private String jmxUrl;

        @JacksonXmlProperty(localName = "mbeanName")
        private String mbeanName;

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getJmxUrl() {
            return jmxUrl;
        }

        public void setJmxUrl(String jmxUrl) {
            this.jmxUrl = jmxUrl;
        }

        public String getMbeanName() {
            return mbeanName;
        }

        public void setMbeanName(String mbeanName) {
            this.mbeanName = mbeanName;
        }
    }

    private JmxXml ensureJmxNode() {
        if (jmxNode == null) {
            jmxNode = new JmxXml();
        }
        if (jmxNode.getTomcat() == null) {
            jmxNode.setTomcat(new JmxServerXml());
        }
        if (jmxNode.getWeblogic() == null) {
            jmxNode.setWeblogic(new JmxServerXml());
        }
        if (jmxNode.getWebsphere() == null) {
            jmxNode.setWebsphere(new JmxServerXml());
        }
        return jmxNode;
    }

    private JmxServerXml ensureTomcatNode() {
        return ensureJmxNode().getTomcat();
    }

    private boolean isActive(JmxServerXml server) {
        return server != null && "true".equalsIgnoreCase(server.getActive());
    }

    private void applyDefaultServer(String actual, String expected, JmxServerXml server) {
        if (server != null) {
            server.setActive(String.valueOf(expected.equalsIgnoreCase(actual)));
        }
    }
}
