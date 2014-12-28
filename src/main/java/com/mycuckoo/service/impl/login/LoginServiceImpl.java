package com.mycuckoo.service.impl.login;

import static com.mycuckoo.common.constant.Common.COMMON_FUN;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_ALL;
import static com.mycuckoo.common.constant.Common.THIRD;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.mycuckoo.common.utils.PwdCrypt;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.commonused.PlatformCommonService;
import com.mycuckoo.service.iface.commonused.UumCommonService;
import com.mycuckoo.service.iface.login.LoginService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 用户登录service
 *
 * @author rutine
 * @time Sep 25, 2014 10:16:53 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private UumCommonService uumCommonService;
	@Autowired
	private PlatformCommonService platformCommonService;
	@Autowired
	private SystemOptLogService sysOptLogService;
	
	@Override
	public void deleteUserCommFunById(List<UumUserCommfun> uumUserCommfunList, 
			HttpServletRequest request) throws ApplicationException {
		
		uumCommonService.deleteUserCommFunById(uumUserCommfunList, request);
	}

	@Override
	public List<UumUserCommfun> findUserCommFunsByUserId(long userId) {
		return uumCommonService.findUserCommFunsByUserId(userId);
	}

	@Override
	public boolean isAdministrator(String userCode) throws ApplicationException {
		// 通过配置XML获得管理员用户，管理员则不需要权限过滤
		SystemConfigXmlParse.getInstance();
		List<String> adminCodes = SystemConfigXmlParse.getSystemConfigBean().getSystemMgr();
		// 管理员
		if(adminCodes.contains(userCode)) return true; 
		
		return false;
	}

	@Override
	public UumUser getUserByUserCodePwd(String userCode, String password) 
			throws ApplicationException {
		
		password = PwdCrypt.getInstance().encrypt(password);//明文加密成密文
		
		return uumCommonService.getUserByUserCodeAPwd(userCode, password);
	}

	@Override
	public Map preLogin(String userCode) throws ApplicationException {
		UumUser user = this.getUserByUserCodePwd(userCode, "");
		Map map = new HashMap();
		if(user != null) {
			long userId = user.getUserId();
			List<UumRoleUserRef> roleUserRefList = uumCommonService.findRoleUsersByUserId(userId);
			List<UumUserAgent> userAgentList = uumCommonService.findUserAgentsByAgentUserId(userId);
			map.put("roleList", roleUserRefList);
			map.put("userAgentList", userAgentList);
		}
		
		return map;
	}

	@Override
	public Map filterPrivilege(Long userId, Long roleId, Long organId, Long organRoleId, 
			String userCode, Long agentId, HttpServletRequest request) throws ApplicationException {
		
		// ====== 1 判断是否为代理 ======
		boolean isAllPrivilege = true; // 是否代理全部权限
		List privilegeIdList = null;
		if(agentId != null) {
			privilegeIdList = uumCommonService.findAgentPrivilegeByAgentId(agentId);
			if(privilegeIdList.size() == 1) {
				String privilegeId = (String) privilegeIdList.get(0);
				if(!PRIVILEGE_SCOPE_ALL.equals(privilegeId)) {
					isAllPrivilege = false;
				}
			} else {
				isAllPrivilege = false;
			}
		}
		
		// ====== 2 加载菜单 ======
		Map map = null;
		if (isAllPrivilege) {
			if (isAdministrator(userCode)) {// 是管理员
				// 加载全部权限
				map = uumCommonService.findUserPrivilegesForAdminLogin();
			} else {// 非管理员
				// 模块权限过滤，用户是否有特殊权限，并过滤特殊权限
				map = uumCommonService.findUserPrivilegesForUserLogin(userId, roleId, organId, organRoleId);
			}
		} else {
			map = uumCommonService.findUserPrivilegesForAgentLogin(userId, roleId, organId, organRoleId, privilegeIdList);
		}
		
		// ====== 3 过滤无效常用功能 ======
		Map thirdMap = (Map) map.get(THIRD);
		// 当前用户常用功能
		List<UumUserCommfun> uumUserCommfunList = findUserCommFunsByUserId(userId);
		// 有效的常用功能
		List<UumUserCommfun> availabilityCommFunList = Lists.newArrayList();
		List<SysplModuleMemu> assignUumUserCommfunList = Lists.newArrayList();
		if (uumUserCommfunList != null && !uumUserCommfunList.isEmpty()) {
			Iterator<Map.Entry<String, List<SysplModuleMemu>>> it = thirdMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<SysplModuleMemu>> entry = it.next();
				List<SysplModuleMemu> secondMenuList = entry.getValue();
				for (UumUserCommfun uumUserCommfun : uumUserCommfunList) {
					for (SysplModuleMemu sysplModuleMemu : secondMenuList) {
						if (uumUserCommfun.getModuleId().longValue() == sysplModuleMemu.getModuleId().longValue()) {
							assignUumUserCommfunList.add(sysplModuleMemu);
							availabilityCommFunList.add(uumUserCommfun);
							break;
						}
					}
				}
			}
		}
		// 移除无效的常用功能数据
		uumUserCommfunList.removeAll(availabilityCommFunList);
		deleteUserCommFunById(uumUserCommfunList, request);
		map.put(COMMON_FUN, assignUumUserCommfunList);
		
		return map;
	}

	@Transactional(readOnly=false)
	@Override
	public void saveLog(String level,String optModName,String optName, String optContent, 
			String optBusinessId, HttpServletRequest request) throws ApplicationException {
		
		sysOptLogService.saveLog(level, optModName, optName, optContent, optBusinessId, request);
	}
}
