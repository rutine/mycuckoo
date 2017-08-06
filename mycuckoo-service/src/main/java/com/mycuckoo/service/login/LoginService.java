package com.mycuckoo.service.login;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.constant.PrivilegeScopeEnum;
import com.mycuckoo.common.utils.PwdCrypt;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserCommfun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.RoleUserService;
import com.mycuckoo.service.uum.UserAgentService;
import com.mycuckoo.service.uum.UserCommFunService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.LoginRoleVo;
import com.mycuckoo.vo.platform.ModuleMemuVo;
import com.mycuckoo.vo.uum.RoleUserRefVo;
import com.mycuckoo.vo.uum.UserAgentVo;

/**
 * 功能说明: 用户登录service
 *
 * @author rutine
 * @time Sep 25, 2014 10:16:53 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class LoginService {
	
	@Autowired
	private UserCommFunService userCommFunService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private UserAgentService userAgentService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private SystemOptLogService sysOptLogService;
	
	
	public void deleteUserCommFunByUserIds(List<UserCommfun> userCommfuns) throws ApplicationException {
		userCommFunService.deleteByUserIds(userCommfuns);
	}

	public List<UserCommfun> findUserCommFunsByUserId(long userId) {
		return userCommFunService.findByUserId(userId);
	}

	public boolean isAdmin(String userCode) throws ApplicationException {
		// 通过配置XML获得管理员用户，管理员则不需要权限过滤
		List<String> adminCodes = SystemConfigXmlParse
				.getInstance().getSystemConfigBean().getSystemMgr();
		// 管理员
		if(adminCodes.contains(userCode)) return true; 
		
		return false;
	}

	public User getUserByUserCodePwd(String userCode, String password) throws ApplicationException {
		password = PwdCrypt.getInstance().encrypt(password);//明文加密成密文
		
		return userService.getUserByUserCodeAndPwd(userCode, password);
	}

	public LoginRoleVo preLogin(User user) throws ApplicationException {
		long userId = user.getUserId();
		List<RoleUserRefVo> roleUserRefs = roleUserService.findByUserId(userId);
		List<UserAgentVo> userAgents = userAgentService.findByAgentUserId(userId);
		
		return new LoginRoleVo(roleUserRefs, userAgents);
	}

	public HierarchyModuleVo filterPrivilege(Long userId, Long roleId, Long organId, Long organRoleId, 
			String userCode, Long agentId) throws ApplicationException {
		
		// ====== 1 判断是否为代理 ======
		boolean isAllPrivilege = true; // 是否代理全部权限
		List privilegeIds = null;
		if(agentId != null) {
			privilegeIds = userAgentService.findPrivilegeIdsByAgentId(agentId);
			if(privilegeIds.size() == 1) {
				String privilegeId = (String) privilegeIds.get(0);
				if(!PrivilegeScopeEnum.ALL.value().equals(privilegeId)) {
					isAllPrivilege = false;
				}
			} else {
				isAllPrivilege = false;
			}
		}
		
		// ====== 2 加载菜单 ======
		HierarchyModuleVo moduleVo = null;
		if (isAllPrivilege) {
			if (isAdmin(userCode)) {// 是管理员
				// 加载全部权限
				moduleVo = privilegeService.findUserPrivilegesForAdminLogin();
			} else {// 非管理员
				// 模块权限过滤，用户是否有特殊权限，并过滤特殊权限
				moduleVo = privilegeService.findUserPrivilegesForUserLogin(userId, roleId, organId, organRoleId);
			}
		} else {
			moduleVo = privilegeService.findUserPrivilegesForAgentLogin(userId, roleId, organId, organRoleId, privilegeIds);
		}
		
		// ====== 3 过滤无效常用功能 ======
		Map<String, List<ModuleMemuVo>> thirdMap = moduleVo.getThird();
		// 当前用户常用功能
		List<UserCommfun> userCommfuns = findUserCommFunsByUserId(userId);
		// 有效的常用功能
		List<UserCommfun> availabilityCommFuns = Lists.newArrayList();
		List<ModuleMemuVo> assignUumUserCommfuns = Lists.newArrayList();
		if (userCommfuns != null && !userCommfuns.isEmpty()) {
			Iterator<Map.Entry<String, List<ModuleMemuVo>>> it = thirdMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<ModuleMemuVo>> entry = it.next();
				List<ModuleMemuVo> secondMenus = entry.getValue();
				for (UserCommfun userCommfun : userCommfuns) {
					for (ModuleMemuVo moduleMemuVo : secondMenus) {
						if (userCommfun.getModuleId().longValue() == moduleMemuVo.getModuleId().longValue()) {
							assignUumUserCommfuns.add(moduleMemuVo);
							availabilityCommFuns.add(userCommfun);
							break;
						}
					}
				}
			}
		}
		// 移除无效的常用功能数据
		userCommfuns.removeAll(availabilityCommFuns);
		deleteUserCommFunByUserIds(userCommfuns);
		moduleVo.setCommonFun(assignUumUserCommfuns);
		
		return moduleVo;
	}

	@Transactional(readOnly=false)
	public void saveLog(LogLevelEnum level, OptNameEnum optModName, String optName, 
			String optContent, String optBusinessId) throws ApplicationException {
		sysOptLogService.saveLog(level, optModName, optName, optContent, optBusinessId);
	}
}
