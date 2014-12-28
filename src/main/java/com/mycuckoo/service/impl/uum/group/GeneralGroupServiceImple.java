package com.mycuckoo.service.impl.uum.group;

import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.ROLE;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.Common.USER;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.GENERAL_GROUP_MGR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumGroupMember;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.group.UumGroupMemberRepository;
import com.mycuckoo.persistence.iface.uum.group.UumGroupRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.RoleService;
import com.mycuckoo.service.iface.uum.UserService;
import com.mycuckoo.service.iface.uum.group.GeneralGroupService;
import com.mycuckoo.service.iface.uum.group.RoleGroupService;
import com.mycuckoo.service.iface.uum.group.UserGroupService;

/**
 * 功能说明: 组管理一般业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:42:18 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class GeneralGroupServiceImple implements GeneralGroupService {
	
	static Logger logger = LoggerFactory.getLogger(GeneralGroupServiceImple.class);
	private UumGroupRepository groupRepository;
	private UumGroupMemberRepository groupMemberRepository;
	
	private RoleGroupService roleGroupService;
	private UserGroupService userGroupService;
	private RoleService roleService;
	private UserService userService;
	private SystemOptLogService sysOptLogService;
	
	@Transactional(readOnly=false)
	@Override
	public boolean disEnableGroup(long groupId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			UumGroup uumGroup = groupRepository.get(groupId);
			groupRepository.updateGroupStatus(groupId, DISABLE);
			writeLog(uumGroup, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			return true;
		} else {
			UumGroup uumGroup = groupRepository.get(groupId);
			groupRepository.updateGroupStatus(groupId, ENABLE);
			writeLog(uumGroup, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public List<UumUser> findGeneralByOrgRolId(long organId, long roleId, String memberType) 
			throws ApplicationException {
		
		List<UumUser>  list = new ArrayList<UumUser>();
		if(USER.equals(memberType)) {
			list = userGroupService.findUsersByOrgRolId(organId, roleId);
		} else {
			List<UumRole> roleList = roleGroupService.findRolesByOrgId(organId);
			// 将roleId转换成userId
			if (roleList != null) {
				for (UumRole uumRole : roleList) {
					UumUser uumUser = new UumUser();
					uumUser.setUserId(uumRole.getRoleId());
					uumUser.setUserName(uumRole.getRoleName());
					list.add(uumUser);
				}
			}
		}
		return list;
	}

	@Override
	public Page<UumGroup> findGeneralGroupByCon(String groupName, Pageable page) {
		List<UumUser> uumUserList = userService.findAllUsers();
		List<UumRole> uumRoleList = roleService.findAllRoles();
		Page<UumGroup> page2 = groupRepository.findGroupsByCon(groupName, page);
		List<UumGroup> groupList = page2.getContent();
		for (UumGroup uumGroup : groupList) {
			List<UumGroupMember> groupMemberList = uumGroup.getUumGroupMembers();
			for (UumGroupMember uumGroupMember : groupMemberList) {
				long memberResourceId = uumGroupMember.getMemberResourceId();
				if (USER.equals(uumGroupMember.getGroupMemberType())) { // 如果是用户
					UumUser newUumUser = null;
					for (UumUser uumUser : uumUserList) {
						if (uumUser.getUserId().longValue() == memberResourceId) {
							newUumUser = uumUser;  break;
						}
					}
					if (newUumUser == null) newUumUser = new UumUser();
					uumGroupMember.setMemberResourceCode(USER);
					uumGroupMember.setMemberResourceName(newUumUser.getUserName());
				} else { // 角色
					UumRole newUumRole = null;
					for (UumRole uumRole : uumRoleList) {
						if (uumRole.getRoleId().longValue() == memberResourceId) {
							newUumRole = uumRole;  break;
						}
					}
					if (newUumRole == null) newUumRole = new UumRole();
					uumGroupMember.setMemberResourceCode(ROLE);
					uumGroupMember.setMemberResourceName(newUumRole.getRoleName());
				}
			}
		}
		
		return page2;
	}
	
  
	@Override
	public UumGroup getGeneralGroupByGroupId(long groupId) {
		List<UumUser> uumUserList = userService.findAllUsers();
		List<UumRole> uumRoleList = roleService.findAllRoles();
		UumGroup group = groupRepository.get(groupId);
		List<UumGroupMember> uumGroupMemberList = group.getUumGroupMembers();
		Iterator<UumGroupMember> iterator = uumGroupMemberList.iterator();
			while (iterator.hasNext()) {
				UumGroupMember uumGroupMember = (UumGroupMember) iterator.next();
				long memberResourceId = uumGroupMember.getMemberResourceId();
				if (USER.equals(uumGroupMember.getGroupMemberType())) { // 如果是用户
					UumUser newUumUser = null;
					for (UumUser uumUser : uumUserList) {
						if (uumUser.getUserId().longValue() == memberResourceId) {
							newUumUser = uumUser;  break;
						}
					}
					if (newUumUser == null) newUumUser = new UumUser();
					uumGroupMember.setMemberResourceName(newUumUser.getUserName());
				} else { // 角色
					UumRole newUumRole = null;
					for (UumRole uumRole : uumRoleList) {
						if (uumRole.getRoleId().longValue() == memberResourceId) {
							newUumRole = uumRole;  break;
						}
					}
					if (newUumRole == null) newUumRole = new UumRole();
					uumGroupMember.setMemberResourceName(newUumRole.getRoleName());
				}
			}
		group.setUumGroupMembers(uumGroupMemberList);
		
		return group;
	}

	@Override
	public boolean existsGeneralGroupByGroupName(String groupName) {
		int count = groupRepository.countGroupsByGroupName(groupName);
		if(count > 0) return true;
		return false;
	}

	@Transactional(readOnly=false)
	@Override
	public void updateGeneralGroup(UumGroup uumGroup, HttpServletRequest request) 
			throws ApplicationException {
		
		groupRepository.deleteGroupMembersByGroupId(uumGroup.getGroupId());
		groupRepository.update(uumGroup);
		groupMemberRepository.save(uumGroup.getUumGroupMembers());
		writeLog(uumGroup, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveGeneralGroup(UumGroup uumGroup, HttpServletRequest request) 
			throws ApplicationException {
		
		groupRepository.save(uumGroup);
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
		sysOptLogService.saveLog(logLevel, GENERAL_GROUP_MGR, opt, optContent, uumGroup.getGroupId() + "", request);
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	@Qualifier(value = "uumGroupRepositoryImpl")
	public void setGroupRepository(UumGroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}
	@Autowired
	public void setGroupMemberRepository(UumGroupMemberRepository groupMemberRepository) {
		this.groupMemberRepository = groupMemberRepository;
	}
	@Autowired
	public void setRoleGroupService(RoleGroupService roleGroupService) {
		this.roleGroupService = roleGroupService;
	}
	@Autowired
	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}
	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}
