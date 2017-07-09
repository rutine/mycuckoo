package com.mycuckoo.service.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.platform.ModuleMemu;
import com.mycuckoo.domain.uum.RoleUserRef;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserAgent;
import com.mycuckoo.domain.uum.UserCommfun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.RoleUserService;
import com.mycuckoo.service.uum.UserAgentService;
import com.mycuckoo.service.uum.UserCommFunService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.TreeVoExtend;

/**
 * 功能说明: 用户常用业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:15:12 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class UumServiceFacade {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private UserCommFunService userCommFunService;
	@Autowired
	private UserAgentService userAgentService;

	public void deletePrivilegeByModOptId(String[] modOptRefIds) {
		this.privilegeService.deletePrivilegeByModOptId(modOptRefIds);
	}

	public void deleteUserCommFunById(List<UserCommfun> userCommfunList) 
			throws ApplicationException {
		
		this.userCommFunService.deleteByUserIds(userCommfunList);
	}

	public List<String> findAgentPrivilegeByAgentId(Long agentId) {
		return this.userAgentService.findPrivilegeIdsByAgentId(agentId);
	}

	public List<RoleUserRef> findRoleUsersByUserId(long userId) {
		return roleUserService.findByUserId(userId);
	}

	public List<UserAgent> findUserAgentsByAgentUserId(long userId) {
		return userAgentService.findByAgentUserId(userId);
	}

	public User getUserByUserCodeAPwd(String userCode, String password) 
			throws ApplicationException {
		
		return userService.getUserByUserCodeAndPwd(userCode, password);
	}

	public List<UserCommfun> findUserCommFunsByUserId(long userId) {
		return userCommFunService.findByUserId(userId);
	}

	public HierarchyModuleVo findUserPrivilegesForAdminLogin() {
		return privilegeService.findUserPrivilegesForAdminLogin();
	}

	public HierarchyModuleVo findUserPrivilegesForAgentLogin(long userId, long roleId, long organId, 
			long organRoleId, List privilegeIdList) {
		
		return privilegeService.findUserPrivilegesForAgentLogin(userId, roleId, organId, 
				organRoleId, privilegeIdList);
	}

	public HierarchyModuleVo findUserPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
		return privilegeService.findUserPrivilegesForUserLogin(userId, roleId, organId, organRoleId);
	}

	public boolean existsSpecialPrivilegeForUser(long userId) {
		return privilegeService.existsSpecialPrivilegeByUserId(userId);
	}

	public List<TreeVoExtend> convertToTree(List<ModuleMemu> moduleMemuList) {
		return privilegeService.convertToTree(moduleMemuList);
	}
}
