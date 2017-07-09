package com.mycuckoo.service.uum.group;

import static com.mycuckoo.common.constant.Common.ROLE;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.Common.USER;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.GENERAL_GROUP_MGR;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.GroupTypeEnum;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.Group;
import com.mycuckoo.domain.uum.GroupMember;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.group.GroupMapper;
import com.mycuckoo.repository.uum.group.GroupMemberMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.service.uum.RoleService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.vo.uum.UserVo;

/**
 * 功能说明: 组管理一般业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:42:18 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class GeneralGroupService {
	
	static Logger logger = LoggerFactory.getLogger(GeneralGroupService.class);
	
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private GroupMemberMapper groupMemberMapper;
	
	@Autowired
	private RoleGroupService roleGroupService;
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
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

	public List<UserVo> findGeneralByOrgRolId(long organId, long roleId, String memberType) 
			throws ApplicationException {
		
		List<UserVo> vos = Lists.newArrayList();
		if(USER.equals(memberType)) {
			vos = userGroupService.findUsersByOrgRolId(organId, roleId);
		} else {
			List<Role> roleList = roleGroupService.findRolesByOrgId(organId);
			// 将roleId转换成userId
			if (roleList != null) {
				for (Role role : roleList) {
					UserVo vo = new UserVo();
					vo.setUserId(role.getRoleId());
					vo.setUserName(role.getRoleName());
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	public Page<Group> findByPage(String groupName, Pageable page) {
		List<UserVo> userList = userService.findAll();
		List<Role> roleList = roleService.findAll();
		
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("groupType", GroupTypeEnum.GENERAL_GROUP.value());
		if (StringUtils.isNotBlank(groupName)) {
			params.put("groupName", "%" + groupName + "%");
		}
		Page<Group> page2 = groupMapper.findByPage(params, page);
		List<Group> groupList = page2.getContent();
		for (Group group : groupList) {
			List<GroupMember> groupMemberList = group.getGroupMembers();
			for (GroupMember groupMember : groupMemberList) {
				long memberResourceId = groupMember.getMemberResourceId();
				if (USER.equals(groupMember.getGroupMemberType())) { // 如果是用户
					User newUser = null;
					for (UserVo user : userList) {
						if (user.getUserId().longValue() == memberResourceId) {
							newUser = user;  break;
						}
					}
					if (newUser == null) newUser = new User();
					groupMember.setMemberResourceCode(USER);
					groupMember.setMemberResourceName(newUser.getUserName());
				} else { // 角色
					Role newRole = null;
					for (Role role : roleList) {
						if (role.getRoleId().longValue() == memberResourceId) {
							newRole = role;  break;
						}
					}
					if (newRole == null) newRole = new Role();
					groupMember.setMemberResourceCode(ROLE);
					groupMember.setMemberResourceName(newRole.getRoleName());
				}
			}
		}
		
		return page2;
	}
	
	public Group getGeneralGroupByGroupId(long groupId) {
		List<UserVo> userList = userService.findAll();
		List<Role> roleList = roleService.findAll();
		Group group = groupMapper.get(groupId);
		List<GroupMember> groupMemberList = group.getGroupMembers();
		Iterator<GroupMember> iterator = groupMemberList.iterator();
			while (iterator.hasNext()) {
				GroupMember groupMember = (GroupMember) iterator.next();
				long memberResourceId = groupMember.getMemberResourceId();
				if (USER.equals(groupMember.getGroupMemberType())) { // 如果是用户
					User newUser = null;
					for (UserVo user : userList) {
						if (user.getUserId().longValue() == memberResourceId) {
							newUser = user;  break;
						}
					}
					if (newUser == null) newUser = new User();
					groupMember.setMemberResourceName(newUser.getUserName());
				} else { // 角色
					Role newRole = null;
					for (Role role : roleList) {
						if (role.getRoleId().longValue() == memberResourceId) {
							newRole = role;  break;
						}
					}
					if (newRole == null) newRole = new Role();
					groupMember.setMemberResourceName(newRole.getRoleName());
				}
			}
		group.setGroupMembers(groupMemberList);
		
		return group;
	}
	
	public boolean existsGeneralGroupByGroupName(String groupName) {
		int count = groupMapper.countByGroupName(groupName, GroupTypeEnum.GENERAL_GROUP.value());
		
		return count > 0;
	}

	@Transactional(readOnly=false)
	public void update(Group group) throws ApplicationException {
		groupMemberMapper.deleteByGroupId(group.getGroupId(), GroupTypeEnum.GENERAL_GROUP.value());
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
		sysOptLogService.saveLog(logLevel, opt, GENERAL_GROUP_MGR, 
				optContent.toString(), group.getGroupId() + "");
	}

}
