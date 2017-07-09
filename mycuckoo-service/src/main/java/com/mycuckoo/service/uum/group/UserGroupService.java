package com.mycuckoo.service.uum.group;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.USER_GROUP_MGR;

import java.util.ArrayList;
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

import com.mycuckoo.common.constant.GroupTypeEnum;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.Group;
import com.mycuckoo.domain.uum.GroupMember;
import com.mycuckoo.domain.uum.RoleUserRef;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.group.GroupMapper;
import com.mycuckoo.repository.uum.group.GroupMemberMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.service.uum.RoleUserService;
import com.mycuckoo.service.uum.UserService;

/**
 * 功能说明: 用户组业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:47:43 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserGroupService {
	
	static Logger logger = LoggerFactory.getLogger(UserGroupService.class);
	
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private GroupMemberMapper groupMemberMapper;
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleUserService roleUserService;
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

	public List<User> findUsersByOrgRolId(long organId, long roleId) throws ApplicationException {
		if(organId != -1 && roleId == -1) {
			List<User> userList = userService.findByOrgId(organId);
			return userList;
		} else {
			Iterator<RoleUserRef> iterator = roleUserService.findByOrgRoleId(organId, roleId).iterator();
			List<User> userList = new ArrayList<User>();
			while (iterator.hasNext()) {
				RoleUserRef uumRoleUserRef = (RoleUserRef) iterator.next();
				userList.add(uumRoleUserRef.getUser());
			}
			
			return userList;
		}
	}

	public Page<Group> findByPage(String groupName,  Pageable page) {
		List<User> uumUserList = userService.findAll();
		
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("groupType", GroupTypeEnum.USER_GROUP.value());
		if (StringUtils.isNotBlank(groupName)) {
			params.put("groupName", "%" + groupName + "%");
		}
		Page<Group> page2 = groupMapper.findByPage(params, page);
		List<Group> groupList = page2.getContent();
		for(Group group : groupList) {
			List<GroupMember> groupMemberList = group.getGroupMembers();
			Iterator<GroupMember> iterator = groupMemberList.iterator();
			while (iterator.hasNext()) {
				GroupMember groupMember = (GroupMember) iterator.next();
				long memberResourceId = groupMember.getMemberResourceId();
				User newUser = null;
				for (User uumUser : uumUserList) {
					if (uumUser.getUserId().longValue() == memberResourceId) {
						newUser = uumUser;
						break;
					}
				}
				if (newUser == null) {
					newUser = new User();
				}
				groupMember.setMemberResourceName(newUser.getUserName());
				groupMember.setMemberResourceCode(newUser.getUserCode());
			}
			group.setGroupMembers(groupMemberList);
		}
		return page2;
	}

	public Group getUserGroupByGroupId(long groupId) {
		List<User> uumUserList = userService.findAll();
		Group group = groupMapper.get(groupId);
		List<GroupMember> groupMemberList = group.getGroupMembers();
		Iterator<GroupMember> iterator = groupMemberList.iterator();
			while (iterator.hasNext()) {
				GroupMember groupMember = (GroupMember) iterator.next();
				long memberResourceId = groupMember.getMemberResourceId();
				User newUser = null;
				for (User uumUser : uumUserList) {
					if (uumUser.getUserId().longValue() == memberResourceId) {
						newUser = uumUser;
						break;
					}
				}
				if (newUser == null) {
					newUser = new User();
				}
				groupMember.setMemberResourceName(newUser.getUserName());
				groupMember.setMemberResourceCode(newUser.getUserCode());
			}
		group.setGroupMembers(groupMemberList);
		
		return group;
	}

	public boolean existsUserGroupByGroupName(String groupName) {
		int count = groupMapper.countByGroupName(groupName, GroupTypeEnum.USER_GROUP.value());
		
		return count > 0;
	}

	@Transactional(readOnly=false)
	public void update(Group group) throws ApplicationException {
		groupMemberMapper.deleteByGroupId(group.getGroupId(), GroupTypeEnum.USER_GROUP.value());
		groupMapper.update(group);
		
		group.getGroupMembers().forEach(memeber -> {
			groupMemberMapper.save(memeber);
		});
		
		writeLog(group, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	public void updateStatus(long groupId, String status) {
		groupMapper.updateStatus(groupId, status);
	}

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
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 4:17:57 PM
	 */
	private void writeLog(Group group, LogLevelEnum logLevel, OptNameEnum opt) throws ApplicationException {
		StringBuilder optContent = new StringBuilder();
		optContent.append("组名称：").append(group.getGroupName()).append(SPLIT);
		optContent.append("组类型：").append(group.getGroupType()).append(SPLIT);
		sysOptLogService.saveLog(logLevel, opt, USER_GROUP_MGR, 
				optContent.toString(), group.getGroupId() + "");
	}

}
