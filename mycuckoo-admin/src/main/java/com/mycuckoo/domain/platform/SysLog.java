package com.mycuckoo.domain.platform;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 功能说明: 系统日志域对象
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 11:00
 */
public class SysLog extends BasicDomain<Long> {

    private Long logId;
    private String title;
    private String content;
    private Integer busiType;
    private String busiId;
    private String ip;
    private String userName;
    private String userRole;

    private Date startTime;
    private Date endTime;

    /**
     * default constructor
     */
    public SysLog() {
    }

    /**
     * minimal constructor
     */
    public SysLog(Long logId) {
        this.logId = logId;
    }

    public Long getLogId() {
        return this.logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? title : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getBusiType() {
        return busiType;
    }

    public void setBusiType(Integer busiType) {
        this.busiType = busiType;
    }

    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}