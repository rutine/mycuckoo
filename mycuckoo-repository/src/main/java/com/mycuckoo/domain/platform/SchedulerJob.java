package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:05:57 PM
 * @version 3.0.0
 */
public class SchedulerJob implements Serializable {

	private Long jobId; //调度ID
	private String jobName; //英文调度名称
	private String jobClassDescript; //调度类名称
	private String triggerType; //触发器类型
	private String timeExpress; //时间表达式
	private Date startTime; //开始时间
	private Date endTime; //结束时间
	private Integer repeatTime; //重复次数
	private Long splitTime; //时间间隔
	private String status; //状态
	private String memo; //备注
	private String creator; //创建人
	private Date createDate; //创建时间

	/** default constructor */
	public SchedulerJob() {
	}

	/** minimal constructor */
	public SchedulerJob(Long jobId, String status) {
		this.jobId = jobId;
		this.status = status;
	}

	/** full constructor */
	public SchedulerJob(Long jobId, String jobName,
			String jobClassDescript, String triggerType, String timeExpress,
			Date startTime, Date endTime, Integer repeatTime, Long splitTime,
			String status, String memo, String creator, Date createDate) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobClassDescript = jobClassDescript;
		this.triggerType = triggerType;
		this.timeExpress = timeExpress;
		this.startTime = startTime;
		this.endTime = endTime;
		this.repeatTime = repeatTime;
		this.splitTime = splitTime;
		this.status = status;
		this.memo = memo;
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

	public String getJobClassDescript() {
		return this.jobClassDescript;
	}

	public void setJobClassDescript(String jobClassDescript) {
		this.jobClassDescript = jobClassDescript;
	}

	public String getTriggerType() {
		return this.triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public String getTimeExpress() {
		return this.timeExpress;
	}

	public void setTimeExpress(String timeExpress) {
		this.timeExpress = timeExpress;
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

	public Integer getRepeatTime() {
		return this.repeatTime;
	}

	public void setRepeatTime(Integer repeatTime) {
		this.repeatTime = repeatTime;
	}

	public Long getSplitTime() {
		return this.splitTime;
	}

	public void setSplitTime(Long splitTime) {
		this.splitTime = splitTime;
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
