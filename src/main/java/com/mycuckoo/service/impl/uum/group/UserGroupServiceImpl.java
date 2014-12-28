package com.mycuckoo.service.impl.uum.group;

import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.USER_GROUP_MGR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumGroupMember;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.group.UumGroupMemberRepository;
import com.mycuckoo.persistence.iface.uum.group.UumUserGroupRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserService;
import com.mycuckoo.service.iface.uum.group.UserGroupService;

/**
 * 功能说明: 用户组业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:47:43 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserGroupServiceImpl implements UserGroupService {
	
	static Logger logger = LoggerFactory.getLogger(UserGroupServiceImpl.class);
	private UumUserGroupRepository userGroupRepository;
	private UumGroupMemberRepository groupMemberRepository;
	
	private UserService userService;
	private RoleUserService roleUserService;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableUserGroup(long groupId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			UumGroup uumGroup = userGroupRepository.get(groupId);
			userGroupRepository.updateUserGroupStatus(groupId, DISABLE);
			writeLog(uumGroup, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			return true;
		} else {
			UumGroup uumGroup = userGroupRepository.get(groupId);
			userGroupRepository.updateUserGroupStatus(groupId, ENABLE);
			writeLog(uumGroup, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public List<UumUser> findUsersByOrgRolId(long organId, long roleId) throws ApplicationException {
		if(organId != -1 && roleId == -1) {
			List<UumUser> userList = userService.findUsersByOrgId(organId);
			return userList;
		} else {
			Iterator<UumRoleUserRef> iterator = roleUserService.findRoleUserRefsByOrgRoleId(organId, roleId).iterator();
			List<UumUser> userList = new ArrayList<UumUser>();
			while (iterator.hasNext()) {
				UumRoleUserRef uumRoleUserRef = (UumRoleUserRef) iterator.next();
				userList.add(uumRoleUserRef.getUumUser());
			}
			
			return userList;
		}
	}

	@Override
	public Page<UumGroup> findUserGroupsByCon(String groupName,  Pageable page) {
		List<UumUser> uumUserList = userService.findAllUsers();
		Page<UumGroup> page2 = userGroupRepository.findUserGroupsByCon(groupName, page);
		List<UumGroup> groupList = page2.getContent();
		for(UumGroup uumGroup : groupList) {
			List<UumGroupMember> uumGroupMemberList = uumGroup.getUumGroupMembers();
			Iterator<UumGroupMember> iterator = uumGroupMemberList.iterator();
			while (iterator.hasNext()) {
				UumGroupMember uumGroupMember = (UumGroupMember) iterator.next();
				long memberResourceId = uumGroupMember.getMemberResourceId();
				UumUser newUumUser = null;
				for (UumUser uumUser : uumUserList) {
					if (uumUser.getUserId().longValue() == memberResourceId) {
						newUumUser = uumUser;
						break;
					}
				}
				if (newUumUser == null) {
					newUumUser = new UumUser();
				}
				uumGroupMember.setMemberResourceName(newUumUser.getUserName());
				uumGroupMember.setMemberResourceCode(newUumUser.getUserCode());
			}
			uumGroup.setUumGroupMembers(uumGroupMemberList);
		}
		return page2;
	}

	@Override
	public UumGroup getUserGroupByGroupId(long groupId) {
		List<UumUser> uumUserList = userService.findAllUsers();
		UumGroup group = userGroupRepository.get(groupId);
		List<UumGroupMember> uumGroupMemberList = group.getUumGroupMembers();
		Iterator<UumGroupMember> iterator = uumGroupMemberList.iterator();
			while (iterator.hasNext()) {
				UumGroupMember uumGroupMember = (UumGroupMember) iterator.next();
				long memberResourceId = uumGroupMember.getMemberResourceId();
				UumUser newUumUser = null;
				for (UumUser uumUser : uumUserList) {
					if (uumUser.getUserId().longValue() == memberResourceId) {
						newUumUser = uumUser;
						break;
					}
				}
				if (newUumUser == null) {
					newUumUser = new UumUser();
				}
				uumGroupMember.setMemberResourceName(newUumUser.getUserName());
				uumGroupMember.setMemberResourceCode(newUumUser.getUserCode());
			}
		group.setUumGroupMembers(uumGroupMemberList);
		
		return group;
	}

	@Override
	public boolean existsUserGroupByGroupName(String groupName) {
		int count = userGroupRepository.countUserGroupsByGroupName(groupName);
		if(count > 0) return true;
		return false;
	}

	@Transactional(readOnly=false)
	@Override
	public void updateUserGroup(UumGroup uumGroup, HttpServletRequest request) 
			throws ApplicationException {
		
		userGroupRepository.deleteUserGroupMembersByGroupId(uumGroup.getGroupId());
		userGroupRepository.update(uumGroup);
		groupMemberRepository.save(uumGroup.getUumGroupMembers());
		writeLog(uumGroup, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Override
	public void updateUserGroupStatus(long groupId, String status, HttpServletRequest request) {
		userGroupRepository.updateUserGroupStatus(groupId, status);
	}

	@Override
	public void saveUserGroup(UumGroup uumGroup, HttpServletRequest request) 
			throws ApplicationException {
		
		userGroupRepository.save(uumGroup);
		groupMemberRepository.save(uumGroup.getUumGroupMembers());
		writeLog(uumGroup, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param uumGroup
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 4:17:57 PM
	 */
	private void writeLog(UumGroup uumGroup, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = "组名称：" + uumGroup.getGroupName() + SPLIT 
			+ "组类型：" + uumGroup.getGroupType() + SPLIT;
		sysOptLogService.saveLog(logLevel, USER_GROUP_MGR, opt, optContent, uumGroup.getGroupId() + "", request);
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setUserGroupRepository(UumUserGroupRepository userGroupRepository) {
		this.userGroupRepository = userGroupRepository;
	}
	@Autowired
	public void setGroupMemberRepository(UumGroupMemberRepository groupMemberRepository) {
		this.groupMemberRepository = groupMemberRepository;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setRoleUserService(RoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}
