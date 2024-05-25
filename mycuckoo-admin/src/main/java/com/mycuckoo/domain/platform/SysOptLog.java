package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:06:08 PM
 */
public class SysOptLog implements Serializable {

    private Long optId;
    private String modName;
    private String name;
    private String content;
    private Integer busiType;
    private String busiId;
    private String host;
    private String ip;
    private String userName;
    private String userRole;
    private String userOrgan;
    private String creator;
    private Date createDate;

    private Date startTime;
    private Date endTime;

    /**
     * default constructor
     */
    public SysOptLog() {
    }

    /**
     * minimal constructor
     */
    public SysOptLog(Long optId) {
        this.optId = optId;
    }

    /**
     * full constructor
     */
    public SysOptLog(Long optId, String modName, String name,
                     String content, Integer busiType, String busiId,
                     String host, String ip, String userName,
                     String userRole, String userOrgan,
                     String creator, Date createDate) {
        this.optId = optId;
        this.modName = modName;
        this.name = name;
        this.content = content;
        this.busiType = busiType;
        this.busiId = busiId;
        this.host = host;
        this.ip = ip;
        this.userName = userName;
        this.userRole = userRole;
        this.userOrgan = userOrgan;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getOptId() {
        return this.optId;
    }

    public void setOptId(Long optId) {
        this.optId = optId;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getUserOrgan() {
        return userOrgan;
    }

    public void setUserOrgan(String userOrgan) {
        this.userOrgan = userOrgan;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
