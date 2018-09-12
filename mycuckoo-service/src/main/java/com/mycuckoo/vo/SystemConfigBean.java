package com.mycuckoo.vo;

import java.util.List;

/**
 * 功能说明: 系统配置参数
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 23, 2014 10:58:53 AM
 */
public class SystemConfigBean {
    private String loggerLevel;
    private String logRecordKeepDays;
    private List<String> systemMgr;
    private String systemName;
    private String cluster;
    private String rowPrivilegeLevel;

    private String tomcatJmxURL;
    private String tomcatObjectName;
    private String jmxDefault;


    private String userAddDelFlag;

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    public String getLogRecordKeepDays() {
        return logRecordKeepDays;
    }

    public void setLogRecordKeepDays(String logRecordKeepDays) {
        this.logRecordKeepDays = logRecordKeepDays;
    }

    public List<String> getSystemMgr() {
        return systemMgr;
    }

    public void setSystemMgr(List<String> systemMgr) {
        this.systemMgr = systemMgr;
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

    public String getTomcatJmxURL() {
        return tomcatJmxURL;
    }

    public void setTomcatJmxURL(String tomcatJmxURL) {
        this.tomcatJmxURL = tomcatJmxURL;
    }

    public String getTomcatObjectName() {
        return tomcatObjectName;
    }

    public void setTomcatObjectName(String tomcatObjectName) {
        this.tomcatObjectName = tomcatObjectName;
    }

    public String getJmxDefault() {
        return jmxDefault;
    }

    public void setJmxDefault(String jmxDefault) {
        this.jmxDefault = jmxDefault;
    }

    public String getRowPrivilegeLevel() {
        return rowPrivilegeLevel;
    }

    public void setRowPrivilegeLevel(String rowPrivilegeLevel) {
        this.rowPrivilegeLevel = rowPrivilegeLevel;
    }

    public String getUserAddDelFlag() {
        return userAddDelFlag;
    }

    public void setUserAddDelFlag(String userAddDelFlag) {
        this.userAddDelFlag = userAddDelFlag;
    }
}
