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
import static com.mycuckoo.common.constant.ServiceVariable.ROLE_GROUP_MGR;

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
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.group.UumGroupMemberRepository;
import com.mycuckoo.persistence.iface.uum.group.UumRoleGroupRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.RoleService;
import com.mycuckoo.service.iface.uum.group.RoleGroupService;

/**
 * 功能说明: 角色组业务类 
 *
 * @author rutine
 * @time Sep 25, 2014 11:44:24 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class RoleGroupServiceImple implements RoleGroupService {
	
	static Logger logger = LoggerFactory.getLogger(RoleGroupServiceImple.class);
	private UumRoleGroupRepository roleGroupRepository;
	private UumGroupMemberRepository groupMemberRepository;
	private RoleService roleService;
	private RoleOrganService roleOrganService;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableGroup(long groupId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			UumGroup uumGroup = roleGroupRepository.get(groupId);
			roleGroupRepository.updateRoleGroupStatus(groupId, DISABLE);
			writeLog(uumGroup, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			return true;
		} else {
			UumGroup uumGroup = roleGroupRepository.get(groupId);
			roleGroupRepository.updateRoleGroupStatus(groupId, ENABLE);
			writeLog(uumGroup, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public List<UumRole> findRolesByOrgId(long organId) throws ApplicationException {
		List<UumRole> roleList = new ArrayList<UumRole>();
		List<UumOrgRoleRef> orgRoleRefList = roleOrganService.findRolesByOrgId(organId);
		if(orgRoleRefList != null) {
			for(UumOrgRoleRef uumOrgRoleRef : orgRoleRefList) {
				roleList.add(uumOrgRoleRef.getUumRole());
			}
		}
		return roleList;
	}

	@Override
	public Page<UumGroup> findRoleGroupsByCon(String groupName, Pageable page) {
		List<UumRole> uumRoleList = roleService.findAllRoles();
		Page<UumGroup> page2 = roleGroupRepository.findRoleGroupsByCon(groupName, page);
		List<UumGroup> groupList = page2.getContent();
		for (UumGroup uumGroup : groupList) {
			List<UumGroupMember> groupMemberList = roleGroupRepository .findRoleGroupMembersByGroupId(uumGroup.getGroupId());
			for (UumGroupMember uumGroupMember : groupMemberList) {
				long memberResourceId = uumGroupMember.getMemberResourceId();
				UumRole newUumRole = null;
				for (UumRole uumRole : uumRoleList) {
					if (uumRole.getRoleId().longValue() == memberResourceId) {
						newUumRole = uumRole;
						break;
					}
				}
				if (newUumRole == null) newUumRole = new UumRole();
				uumGroupMember.setMemberResourceName(newUumRole.getRoleName());
				uumGroupMember.setMemberResourceCode(newUumRole.getRoleId() + "");
			}
			uumGroup.setUumGroupMembers(groupMemberList);
		}
		
		return page2;
	}
	
	@Override
	public UumGroup getRoleGroupById(long groupId) {
		List<UumRole> uumRoleList = roleService.findAllRoles();
		UumGroup group = roleGroupRepository.get(groupId);
		 List<UumGroupMember> uumGroupMemberList = roleGroupRepository .findRoleGroupMembersByGroupId(group.getGroupId());
		 Iterator<UumGroupMember> iterator = uumGroupMemberList.iterator();
			while (iterator.hasNext()) {
				UumGroupMember uumGroupMember = (UumGroupMember) iterator.next();
				long memberResourceId = uumGroupMember.getMemberResourceId();
				UumRole newUumRole = null;
				for (UumRole uumRole : uumRoleList) {
					if (uumRole.getRoleId().longValue() == memberResourceId) {
						newUumRole = uumRole;
						break;
					}
				}
				if (newUumRole == null) newUumRole = new UumRole();
				uumGroupMember.setMemberResourceName(newUumRole.getRoleName());
				uumGroupMember.setMemberResourceCode(newUumRole.getRoleId() + "");
			}
		group.setUumGroupMembers(uumGroupMemberList);
		
		return group;
	}
	
	@Override
	public boolean existsRoleGroupByGroupName(String groupName) {
		int count = roleGroupRepository.countRoleGroupsByGroupName(groupName);
		if(count > 0) return true;
		return false;
	}

	@Transactional(readOnly=false)
	@Override
	public void updateRoleGroup(UumGroup uumGroup, HttpServletRequest request) 
			throws ApplicationException {
		
		roleGroupRepository.deleteRoleGroupMembersByGroupId(uumGroup.getGroupId());
		roleGroupRepository.update(uumGroup);
		groupMemberRepository.save(uumGroup.getUumGroupMembers());
		writeLog(uumGroup, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveRoleGroup(UumGroup uumGroup, HttpServletRequest request) 
			throws ApplicationException {
		
		roleGroupRepository.save(uumGroup);
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
		sysOptLogService.saveLog(logLevel, ROLE_GROUP_MGR, opt, optContent, uumGroup.getGroupId() + "", request);
	}

	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setRoleGroupRepository(UumRoleGroupRepository roleGroupRepository) {
		this.roleGroupRepository = roleGroupRepository;
	}
	@Autowired
	public void setGroupMemberRepository(UumGroupMemberRepository groupMemberRepository) {
		this.groupMemberRepository = groupMemberRepository;
	}
	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
	public void setRoleOrganService(RoleOrganService roleOrganService) {
		this.roleOrganService = roleOrganService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	
}
