package com.mycuckoo.service.uum.group;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ROLE_GROUP_MGR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.constant.GroupTypeEnum;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.Group;
import com.mycuckoo.domain.uum.GroupMember;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.group.GroupMapper;
import com.mycuckoo.repository.uum.group.GroupMemberMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.service.uum.RoleOrganService;
import com.mycuckoo.service.uum.RoleService;

/**
 * 功能说明: 角色组业务类 
 *
 * @author rutine
 * @time Sep 25, 2014 11:44:24 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class RoleGroupService {
	static Logger logger = LoggerFactory.getLogger(RoleGroupService.class);
	
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private GroupMemberMapper groupMemberMapper;
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleOrganService roleOrganService;
	@Autowired
	private SystemOptLogService sysOptLogService;

	
	@Transactional(readOnly=false)
	public boolean disEnable(long groupId, String disEnableFlag) throws ApplicationException {
		if(DISABLE.equals(disEnableFlag)) {
			Group group = groupMapper.get(groupId);
			groupMapper.updateStatus(groupId, DISABLE);
			
			writeLog(group, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
			return true;
		} else {
			Group group = groupMapper.get(groupId);
			groupMapper.updateStatus(groupId, ENABLE);
			
			writeLog(group, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
			return true;
		}
	}

	public List<Role> findRolesByOrgId(long organId) throws ApplicationException {
		List<Role> roleList = new ArrayList<Role>();
		List<OrgRoleRef> orgRoleRefList = roleOrganService.findRolesByOrgId(organId);
		for(OrgRoleRef uumOrgRoleRef : orgRoleRefList) {
			roleList.add(uumOrgRoleRef.getRole());
		}
		
		return roleList;
	}

	public Page<Group> findByPage(String groupName, Pageable page) {
		List<Role> uumRoleList = roleService.findAll();
		
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("groupType", GroupTypeEnum.ROLE_GROUP.value());
		if (StringUtils.isNotBlank(groupName)) {
			params.put("groupName", "%" + groupName + "%");
		}
		Page<Group> page2 = groupMapper.findByPage(params, page);
		List<Group> groupList = page2.getContent();
		for (Group group : groupList) {
			List<GroupMember> groupMemberList = groupMemberMapper.findByGroupId(group.getGroupId(), null);
			for (GroupMember groupMember : groupMemberList) {
				long memberResourceId = groupMember.getMemberResourceId();
				Role newRole = null;
				for (Role uumRole : uumRoleList) {
					if (uumRole.getRoleId().longValue() == memberResourceId) {
						newRole = uumRole;
						break;
					}
				}
				if (newRole == null) newRole = new Role();
				groupMember.setMemberResourceName(newRole.getRoleName());
				groupMember.setMemberResourceCode(newRole.getRoleId() + "");
			}
			group.setGroupMembers(groupMemberList);
		}
		
		return page2;
	}
	
	
	public Group get(long groupId) {
		List<Role> roleList = roleService.findAll();
		Group group = groupMapper.get(groupId);
		List<GroupMember> groupMemberList = groupMemberMapper.findByGroupId(group.getGroupId(), null);
		groupMemberList.forEach(groupMember -> {
			long memberResourceId = groupMember.getMemberResourceId();
			Role newRole = null;
			for (Role role : roleList) {
				if (role.getRoleId().longValue() == memberResourceId) {
					newRole = role;
					break;
				}
			}
			if (newRole == null) newRole = new Role();
			groupMember.setMemberResourceName(newRole.getRoleName());
			groupMember.setMemberResourceCode(newRole.getRoleId() + "");
		});
		group.setGroupMembers(groupMemberList);
		
		return group;
	}
	
	public boolean existsByGroupName(String groupName) {
		int count = groupMapper.countByGroupName(groupName, GroupTypeEnum.ROLE_GROUP.value());
		
		return count > 0;
	}

	@Transactional(readOnly=false)
	public void update(Group group) throws ApplicationException {
		groupMemberMapper.deleteByGroupId(group.getGroupId(), GroupTypeEnum.ROLE_GROUP.value());
		groupMapper.update(group);
		
		group.getGroupMembers().forEach(memeber -> {
			groupMemberMapper.save(memeber);
		});
		
		writeLog(group, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	@Transactional(readOnly=false)
	public void save(Group group) throws ApplicationException {
		groupMapper.save(group);
		
		group.getGroupMembers().forEach(memeber -> {
			groupMemberMapper.save(memeber);
		});
		
		writeLog(group, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param group
	 * @param logLevel
	 * @param opt
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 4:17:57 PM
	 */
	private void writeLog(Group group, LogLevelEnum logLevel, OptNameEnum opt) throws ApplicationException {
		StringBuilder optContent = new StringBuilder();
		optContent.append("组名称：").append(group.getGroupName()).append(SPLIT);
		optContent.append("组类型：").append(group.getGroupType()).append(SPLIT);
		sysOptLogService.saveLog(logLevel, opt, ROLE_GROUP_MGR, 
				optContent.toString(), group.getGroupId() + "");
	}
	
}
