package com.mycuckoo.domain.uum;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:41:33 AM
 * @version 3.0.0
 */
public class Group {

	private Long groupId; //组ID
	private String groupType; 
	private String groupName; //组名称
	private String memo; //备注
	private String status; //组状态
	private String creator; //创建人
	private Date createDate; //创建时间
	private List<GroupMember> groupMembers = Lists.newArrayList();

	/** default constructor */
	public Group() {
	}

	/** minimal constructor */
	public Group(Long groupId, String status) {
		this.groupId = groupId;
		this.status = status;
	}

	/** full constructor */
	public Group(Long groupId, String groupType, String groupName,
			String memo, String status, String creator, Date createDate,
			List<GroupMember> groupMembers) {
		this.groupId = groupId;
		this.groupType = groupType;
		this.groupName = groupName;
		this.memo = memo;
		this.status = status;
		this.creator = creator;
		this.createDate = createDate;
		this.groupMembers = groupMembers;
	}

	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupType() {
		return this.groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<GroupMember> getGroupMembers() {
		return this.groupMembers;
	}

	public void setGroupMembers(List<GroupMember> groupMembers) {
		this.groupMembers = groupMembers;
	}
}
