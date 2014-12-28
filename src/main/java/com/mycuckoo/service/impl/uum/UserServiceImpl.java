package com.mycuckoo.service.impl.uum;

import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ROLE_CSS;
import static com.mycuckoo.common.constant.ServiceVariable.USER_MGR;
import static com.mycuckoo.common.constant.ServiceVariable.Y;
import static com.mycuckoo.common.utils.CommonUtils.encrypt;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumUserRepository;
import com.mycuckoo.service.iface.commonused.UserCommFunService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.OrganService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserAgentService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 用户业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:37:54 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserServiceImpl implements UserService {
	
	static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private UumUserRepository userRepository;
	private UserAgentService userAgentService;
	private RoleUserService roleUserService;
	private OrganService organService;
	private RoleOrganService roleOrganService;
	private PrivilegeService privilegeService;
	private SystemOptLogService sysOptLogService;
	private UserCommFunService userCommFunService;
	

	/**
	 * 停用/启用用户 删除用户所属角色并移到无角色用户 删除用户所拥有操作权限
	 *
	 * @param userId 用户ID
	 * @param disEnableFlag 停用启用标志
	 * @return boolean true成功
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 3:50:29 PM
	 * 
	 * 1 移除用户角色 
	 * 2 移除用户代理 
	 * 3 移除用户代理权限 
	 * 4 移除用户常用功能 
	 * 5 移除用户操作行权限
	 */
	@Override
	public boolean disEnableUser(long userId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			UumUser uumUser = getUserByUserId(userId);
			uumUser.setStatus(DISABLE);
			UumOrgan uumOrgan = new UumOrgan(0l, null);
			uumUser.setUumOrgan(uumOrgan);
			updateUser(uumUser, request); // 更改用户所属机构为0
			roleUserService.deleteRoleUserRefByUserId(userId, request); // 1 移除用户角色
			
			UumRoleUserRef uumRoleUserRef = new UumRoleUserRef();
			uumRoleUserRef.setUumUser(uumUser);
			uumRoleUserRef.setIsDefault(Y);
			UumOrgRoleRef uumOrgRoleRef = new UumOrgRoleRef();
			uumOrgRoleRef.setOrgRoleId(0l);
			uumRoleUserRef.setUumOrgRoleRef(uumOrgRoleRef);
			roleUserService.saveRoleUserRef(uumRoleUserRef); // 设置无角色用户
			privilegeService.deletePrivilegeByOwnerIdAType(userId, OWNER_TYPE_USR, request); // 5 移除用户所拥有操作、行权限
			userAgentService.deleteUserAgentsByUserId(userId, request); // 2 移除用户代理 3 移除用户代理权限
			userCommFunService.deleteUserCommFunByUserId(userId, request); // 4 移除用户常用功能
			writeLog(uumUser, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			return true;
		} else {
			UumUser uumUser = getUserByUserId(userId);
			uumUser.setStatus(ENABLE);
			updateUser(uumUser, request);
			writeLog(uumUser, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public List<UumUser> findAllUsers() {
		return (List<UumUser>) userRepository.findAll();
	}

	@Override
	public List<UumUser> findUsersByUserName(String userName) {
		return userRepository.findUsersByUserName(userName);
	}

	@Override
	public List findUsersByUserNamePy(String userNamePy, long userId) {
		return userRepository.findUsersByUserNamePy(userNamePy, userId);
	}

	@Override
	public List<? extends TreeVo> findNextLevelChildNodes(String treeId, String isCheckbox) {
		int temp = treeId.indexOf("_");
		int orgId = 0;
		if(temp > 0) {
			orgId = Integer.parseInt(treeId.substring(0, temp));
		}
		
		List<UumOrgRoleRef> orgRoleList = roleOrganService.findRolesByOrgId(orgId);
		List<TreeVoExtend> orgList = organService.findNextLevelChildNodesWithCheckbox(orgId, 0);
		
		for(TreeVoExtend treeVoExt : orgList) {
			treeVoExt.setNocheck(true); // 机构无checkbox
			treeVoExt.setId(treeVoExt.getId() + "_1"); 
		}
		
		for(UumOrgRoleRef uumOrgRoleRef : orgRoleList) {
			UumRole uumRole = uumOrgRoleRef.getUumRole();
			TreeVoExtend treeVoExt = new TreeVoExtend();
			if(!Y.equals(isCheckbox)) {
				// 角色无checkbox
				treeVoExt.setNocheck(true);
				treeVoExt.setIconSkin(ROLE_CSS);
			} 
			treeVoExt.setId(uumOrgRoleRef.getOrgRoleId() + "_2");
			treeVoExt.setText(uumRole.getRoleName());
			treeVoExt.setLeaf(true);
			
			orgList.add(treeVoExt);
		}
		
		return orgList;
	}

	@Override
	public Page<UumUser> findUsersByCon(String treeId, String userName, String userCode, Pageable page) {
		List<Long> orgIdList = new ArrayList<Long>();
		if(!isNullOrEmpty(treeId) && treeId.length() >= 3) {
			int orgId = Integer.parseInt(treeId.substring(0, treeId.indexOf("_")));
			List<UumOrgan> organList = organService.findChildNodes(orgId, 0);
			for(UumOrgan uumOrgan : organList) {
				orgIdList.add(uumOrgan.getOrgId());
			}
		}
		
		return userRepository.findUsersByCon(treeId, orgIdList.toArray(new Long[orgIdList.size()]), userCode, userName, page);
	}

	@Override
	public List<UumUser> findUsersByOrgId(long organId) {
		return userRepository.findUsersByOrgId(organId);
	}

	@Override
	public UumUser getUserByUserCodeAndPwd(String userCode, String password) throws ApplicationException {
		if(isNullOrEmpty(userCode) || isNullOrEmpty(userCode)) return null;
		try {
			UumUser uumUser = userRepository.getUserByUserCodeAndPwd(userCode, password);
			return uumUser;
		} catch(Exception e) {
			logger.error("用户编码重复错误!", e);
			
			throw new ApplicationException("用户编码已存在错误!");
		}
	}

	@Override
	public UumUser getUserByUserId(long userId) {
		UumUser uumUser = userRepository.get(userId);
		uumUser.setBelongOrganId(uumUser.getUumOrgan().getOrgId());
		
		List<UumRoleUserRef> roleUserList = roleUserService.findRoleUserRefsByUserId(uumUser.getUserId());
		if(roleUserList != null && !roleUserList.isEmpty()) {
			for(UumRoleUserRef uumRoleUserRef : roleUserList) {
				if(Y.equals(uumRoleUserRef.getIsDefault())) {
					long orgRoleId = uumRoleUserRef.getUumOrgRoleRef().getOrgRoleId();
					String roleName = uumRoleUserRef.getUumOrgRoleRef().getUumRole().getRoleName();
					
					uumUser.setUumOrgRoleId(orgRoleId);
					uumUser.setUumRoleName(roleName);
					break;
				}
			}
		}
		
		return uumUser;
	}

	@Override
	public List findUsersByUserIds(Long[] userIds) {
		if(userIds == null || userIds.length == 0) return null;
		
		return userRepository.findUsersByUserIds(userIds);
	}

	@Override
	public Page<UumUser> findUsersForSetAdmin(String userName, String userCode, Pageable page) {
//		Map<String, Object> map = new WeakHashMap<String, Object>();
//		Page<UumUser> page2 = userRepository.findUsersByConditions("", null, userCode, userName, page);
//		List<UumUser> userList = page2.getContent();
//		int count = (int) page2.getTotalElements();
//		List<UumUser> systemAdminUserList = new ArrayList<UumUser>();
//		SystemConfigXmlParse.getInstance();
//		List<String> systemAdminCode = SystemConfigXmlParse.getSystemConfigBean().getSystemMgr();
//		int systemAdminUserCount = 0;
//		if(userList != null && !userList.isEmpty()) {
//			for(UumUser uumUser : userList) {
//				for(String adminCode : systemAdminCode) {
//					if(uumUser.getUserCode().equals(adminCode)) {
//						systemAdminUserList.add(uumUser);
//						systemAdminUserCount++;
//					}
//				}
//			}
//			userList.removeAll(systemAdminUserList);
//			count = count - systemAdminUserCount;
//			// 可分配为管理员的用户
//			map.put("userList", userList);
//			map.put("userCount", count);
//		}
//		// 已分配为管理员的用户
//		map.put("systemAdminUserList", systemAdminUserList);
//		map.put("systemAdminUserCount", systemAdminUserCount);
//		
//		return map;
	
		Page<UumUser> page2 = userRepository.findUsersByCon("", null, userCode, userName, page);
		List<UumUser> userList = new ArrayList<UumUser>();
		SystemConfigXmlParse.getInstance();
		List<String> systemAdminCode = SystemConfigXmlParse.getSystemConfigBean().getSystemMgr();
		int count = 0;
		for (UumUser uumUser : page2.getContent()) {
			if (!systemAdminCode.contains(uumUser.getUserCode())) {
				count++;
				userList.add(uumUser);
			}	
		}
		
		return new PageImpl<UumUser>(userList, page, page2.getTotalElements() - count);
	}
	
	@Override
	public Page<UumUser> findAdminUsers() {
		SystemConfigXmlParse.getInstance();
		List<String> systemAdminCode = SystemConfigXmlParse.getSystemConfigBean().getSystemMgr();
		List<UumUser> systemAdminUserList = new ArrayList<UumUser>();
		for(String userCode : systemAdminCode) {
			try {
				systemAdminUserList.add(userRepository.getUserByUserCodeAndPwd(userCode, null));
			} catch(Exception e) {
				logger.warn("用户编码'{}'存在重复!", userCode);
			}
		}
		
		return new PageImpl<UumUser>(systemAdminUserList);
	}

	@Override
	public boolean existsUserByUserCode(String userCode) {
		return userRepository.existsUserByUserCode(userCode);
	}

	@Override
	public void updateUser(UumUser uumUser, HttpServletRequest request) throws ApplicationException {
		// 设置用户所属机构
		UumOrgRoleRef uumOrgRoleRef = roleOrganService
				.getOrgRoleRefById(uumUser.getUumOrgRoleId() == null ? 0 : uumUser.getUumOrgRoleId());
		uumUser.setUumOrgan(uumOrgRoleRef.getUumOrgan());
		uumUser.setUserPassword(encrypt(uumUser.getUserPassword())); // 加密
		
		userRepository.update(uumUser); // 保存用户
		writeLog(uumUser, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Override
	public void updateUserBelongOrgIdAssignRole(long organId, long userId, HttpServletRequest request) {
		userRepository.updateUserBelongOrgIdByUserId(organId, userId);
	}

	
	@Override
	public void updateUserInfo(UumUser uumUser, HttpServletRequest request) 
			throws ApplicationException {
		
		UumUser uumUser2 = userRepository.get(uumUser.getUserId());
		uumUser2.setUserCode(uumUser.getUserCode());
		uumUser2.setUserName(uumUser.getUserName());
		uumUser2.setUserPassword(uumUser.getUserPassword());
		userRepository.update(uumUser); // 保存用户
	}

	@Transactional(readOnly = false)
	@Override
	public void updateUserPhotoUrl(String photoUrl, long userId, HttpServletRequest request) 
			throws ApplicationException {
		
		userRepository.updateUserPhotoUrlByUserId(photoUrl, userId);
	}

	@Transactional(readOnly = false)
	@Override
	public void resetPwdByUserId(String userDefaultPwd, String userName, long userId, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = "重置密码用户：" + userName;
		
		userRepository.updateUserPwdByUserId(encrypt(userDefaultPwd), userId);
		sysOptLogService.saveLog(LOG_LEVEL_SECOND, USER_MGR, "重置密码", optContent, userId + "", request);
	}

	@Override
	public void saveUser(UumUser uumUser, HttpServletRequest request) throws ApplicationException {
		// 设置用户所属机构
		UumOrgRoleRef uumOrgRoleRef = roleOrganService.getOrgRoleRefById(uumUser.getUumOrgRoleId());
		uumUser.setUumOrgan(uumOrgRoleRef.getUumOrgan());
		uumUser.setUserPassword(encrypt(uumUser.getUserPassword())); // 加密
		
		UumRoleUserRef uumRoleUserRef = new UumRoleUserRef(); // 默认角色
		uumRoleUserRef.setIsDefault(Y);
		uumRoleUserRef.setUumOrgRoleRef(uumOrgRoleRef);
		uumRoleUserRef.setUumUser(uumUser);
		
		roleUserService.saveRoleUserRef(uumRoleUserRef); // 保存用户的默认角色
		userRepository.save(uumUser);
		writeLog(uumUser, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param uumUser
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 4:17:57 PM
	 */
	private void writeLog(UumUser uumUser, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = uumUser.getUserCode() + SPLIT 
			+ uumUser.getUserName() + SPLIT 
			+ uumUser.getUumRoleName() + SPLIT;
		sysOptLogService.saveLog(logLevel, USER_MGR, opt, optContent, uumUser.getUserId() + "", request);
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setUserRepository(UumUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Autowired
	public void setUserAgentService(UserAgentService userAgentService) {
		this.userAgentService = userAgentService;
	}
	@Autowired
	public void setRoleUserService(RoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}
	@Autowired
	public void setOrganService(OrganService organService) {
		this.organService = organService;
	}
	@Autowired
	public void setRoleOrganService(RoleOrganService roleOrganService) {
		this.roleOrganService = roleOrganService;
	}
	@Autowired
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	@Autowired
	public void setUserCommFunService(UserCommFunService userCommFunService) {
		this.userCommFunService = userCommFunService;
	}
	
}
