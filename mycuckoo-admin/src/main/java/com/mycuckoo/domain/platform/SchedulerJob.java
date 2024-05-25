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
 * @time Sep 22, 2014 9:05:57 PM
 */
public class SchedulerJob implements Serializable {

    private Long jobId; //调度ID
    private String jobName; //英文调度名称
    private String jobClass; //调度类名称
    private String triggerType; //触发器类型
    private String cron; //时间表达式
    private Date startTime; //开始时间
    private Date endTime; //结束时间
    private Integer repeatCount; //重复次数
    private Long intervalTime; //时间间隔
    private String status; //状态
    private String memo; //备注
    private String updater; //更新人
    private Date updateDate; //更新时间
    private String creator; //创建人
    private Date createDate; //创建时间

    /**
     * default constructor
     */
    public SchedulerJob() {
    }

    /**
     * minimal constructor
     */
    public SchedulerJob(Long jobId, String status) {
        this.jobId = jobId;
        this.status = status;
    }

    /**
     * full constructor
     */
    public SchedulerJob(Long jobId, String jobName,
                        String jobClass, String triggerType, String cron,
                        Date startTime, Date endTime, Integer repeatCount, Long intervalTime,
                        String status, String memo, String updater, Date updateDate, String creator, Date createDate) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobClass = jobClass;
        this.triggerType = triggerType;
        this.cron = cron;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatCount = repeatCount;
        this.intervalTime = intervalTime;
        this.status = status;
        this.memo = memo;
        this.updater = updater;
        this.updateDate = updateDate;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getJobId() {
        return this.jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getTriggerType() {
        return this.triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
