package com.mycuckoo.domain.uum;

import java.util.Comparator;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:41:50 AM
 * @version 3.0.0
 */
public class GroupMember  implements java.io.Serializable {

	private Long groupMemberId;
	private Group group;
	private String groupMemberType;
	private Long memberResourceId;
	
	private String memberResourceName;
	private String memberResourceCode;

	/** default constructor */
	public GroupMember() {
	}

	/** minimal constructor */
	public GroupMember(Long groupMemberId) {
		this.groupMemberId = groupMemberId;
	}

	/** full constructor */
	public GroupMember(Long groupMemberId, Group group,
			String groupMemberType, Long memberResourceId) {
		this.groupMemberId = groupMemberId;
		this.group = group;
		this.groupMemberType = groupMemberType;
		this.memberResourceId = memberResourceId;
	}

	public Long getGroupMemberId() {
		return this.groupMemberId;
	}

	public void setGroupMemberId(Long groupMemberId) {
		this.groupMemberId = groupMemberId;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setUumGroup(Group group) {
		this.group = group;
	}

	public String getGroupMemberType() {
		return this.groupMemberType;
	}

	public void setGroupMemberType(String groupMemberType) {
		this.groupMemberType = groupMemberType;
	}

	public Long getMemberResourceId() {
		return this.memberResourceId;
	}

	public void setMemberResourceId(Long memberResourceId) {
		this.memberResourceId = memberResourceId;
	}

	public String getMemberResourceName() {
		return memberResourceName;
	}

	public String getMemberResourceCode() {
		return memberResourceCode;
	}

	public void setMemberResourceName(String memberResourceName) {
		this.memberResourceName = memberResourceName;
	}

	public void setMemberResourceCode(String memberResourceCode) {
		this.memberResourceCode = memberResourceCode;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj) return true;		 // (1) same object?
		if (obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		GroupMember uumGroupMember = (GroupMember)obj;
		if(groupMemberId != null && uumGroupMember.getGroupMemberId() != null && 
			groupMemberId.longValue() == uumGroupMember.getGroupMemberId().longValue())
			return true;
		return false;
	}
		
	@Override
	public int hashCode() {
		int hashcode = 7;
		
		hashcode = hashcode * 37 + (groupMemberId == null ? 0 : groupMemberId.hashCode());
		
		return hashcode;
	}
	
	public GroupMemberComp getGroupMemberCompInst(){
		return new GroupMemberComp();
	}
	
	class GroupMemberComp implements Comparator<GroupMember>{
		public int compare(GroupMember groupMember1, GroupMember groupMember2) {
			return groupMember1.getGroupMemberId().compareTo(groupMember2.getGroupMemberId());
		}
	}

}
