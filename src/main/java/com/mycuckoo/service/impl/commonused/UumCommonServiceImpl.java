package com.mycuckoo.service.impl.commonused;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.commonused.UserCommFunService;
import com.mycuckoo.service.iface.commonused.UumCommonService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserAgentService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 用户常用业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:15:12 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class UumCommonServiceImpl implements UumCommonService {
	
	private UserService userService;
	private RoleUserService roleUserService;
	private PrivilegeService privilegeService;
	private UserCommFunService userCommFunService;
	private UserAgentService userAgentService;

	@Override
	public void deletePrivilegeByModOptId(String[] modOptRefIds, HttpServletRequest request) {
		this.privilegeService.deletePrivilegeByModOptId(modOptRefIds, request);
	}

   
	@Override
	public void deleteUserCommFunById(List<UumUserCommfun> uumUserCommfunList, HttpServletRequest request) 
			throws ApplicationException {
		
		this.userCommFunService.deleteUserCommFunByUserIds(uumUserCommfunList, request);
	}

	
	@Override
	public List<String> findAgentPrivilegeByAgentId(Long agentId) {
		return this.userAgentService.findPrivilegeIdsByAgentId(agentId);
	}

	@Override
	public List<UumRoleUserRef> findRoleUsersByUserId(long userId) {
		return roleUserService.findRoleUserRefsByUserId(userId);
	}

	@Override
	public List<UumUserAgent> findUserAgentsByAgentUserId(long userId) {
		return userAgentService.findUserAgentsByAgentUserId(userId);
	}

	@Override
	public UumUser getUserByUserCodeAPwd(String userCode, String password) 
			throws ApplicationException {
		
		return userService.getUserByUserCodeAndPwd(userCode, password);
	}

	@Override
	public List<UumUserCommfun> findUserCommFunsByUserId(long userId) {
		return userCommFunService.findUserCommFunByUserId(userId);
	}

	@Override
	public Map findUserPrivilegesForAdminLogin() {
		return privilegeService.findUserPrivilegesForAdminLogin();
	}

	@Override
	public Map findUserPrivilegesForAgentLogin(long userId, long roleId, long organId, 
			long organRoleId, List privilegeIdList) {
		
		return privilegeService.findUserPrivilegesForAgentLogin(userId, roleId, organId, 
				organRoleId, privilegeIdList);
	}

	@Override
	public Map findUserPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
		return privilegeService.findUserPrivilegesForUserLogin(userId, roleId, organId, organRoleId);
	}

	@Override
	public boolean existsSpecialPrivilegeForUser(long userId) {
		return privilegeService.existsSpecialPrivilegeByUserId(userId);
	}

	@Override
	public List<TreeVoExtend> convertToTree(List<SysplModuleMemu> moduleMemuList) {
		return privilegeService.convertToTree(moduleMemuList);
	}

	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setRoleUserService(RoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}
	@Autowired
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}
	@Autowired
	public void setUserCommFunService(UserCommFunService userCommFunService) {
		this.userCommFunService = userCommFunService;
	}
	@Autowired
	public void setUserAgentService(UserAgentService userAgentService) {
		this.userAgentService = userAgentService;
	}

}
