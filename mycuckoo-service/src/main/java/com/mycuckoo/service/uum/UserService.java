package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ROLE_CSS;
import static com.mycuckoo.common.constant.ServiceVariable.USER_MGR;
import static com.mycuckoo.common.constant.ServiceVariable.Y;
import static com.mycuckoo.common.utils.CommonUtils.encrypt;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.RoleUserRef;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.UserMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.TreeVoExtend;
import com.mycuckoo.vo.uum.OrganVo;
import com.mycuckoo.vo.uum.RoleUserRefVo;
import com.mycuckoo.vo.uum.UserVo;

/**
 * 功能说明: 用户业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:37:54 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserService {
	static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserAgentService userAgentService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private OrganService organService;
	@Autowired
	private RoleOrganService roleOrganService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private SystemOptLogService sysOptLogService;
	@Autowired
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
	public boolean disEnable(long userId, String disEnableFlag) throws ApplicationException {
		if(DISABLE.equals(disEnableFlag)) {
			User user = getUserByUserId(userId);
			user.setStatus(DISABLE);
			user.setUserBelongtoOrg(0L);
			userMapper.update(user); // 更改用户所属机构为0
			roleUserService.deleteByUserId(userId); // 1 移除用户角色
			
			RoleUserRef roleUserRef = new RoleUserRef();
			roleUserRef.setUser(user);
			roleUserRef.setIsDefault(Y);
			OrgRoleRef orgRoleRef = new OrgRoleRef();
			orgRoleRef.setOrgRoleId(0l);
			roleUserRef.setOrgRoleRef(orgRoleRef);
			roleUserService.save(roleUserRef); // 设置无角色用户
			privilegeService.deleteByOwnerIdAndOwnerType(userId, OWNER_TYPE_USR); // 5 移除用户所拥有操作、行权限
			userAgentService.deleteUserAgentsByUserId(userId); // 2 移除用户代理 3 移除用户代理权限
			userCommFunService.deleteByUserId(userId); // 4 移除用户常用功能
			
			writeLog(user, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
			return true;
		} else {
			User user = getUserByUserId(userId);
			user.setStatus(ENABLE);
			userMapper.update(user);
			
			writeLog(user, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
			return true;
		}
	}

	public List<UserVo> findAll() {
		List<User> list = userMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
		List<UserVo> vos = Lists.newArrayList();
		list.forEach(entity -> {
			UserVo vo = new UserVo();
			BeanUtils.copyProperties(entity, vo);
			vos.add(vo);
		});
		
		return vos;
	}

	public List<UserVo> findByUserName(String userName) {
		List<User> list =  userMapper.findByUserName(userName);
		
		List<UserVo> vos = Lists.newArrayList();
		list.forEach(entity -> {
			UserVo vo = new UserVo();
			BeanUtils.copyProperties(entity, vo);
			vos.add(vo);
		});
		
		return vos;
	}

	public List findByUserNamePy(String userNamePy, long userId) {
		return userMapper.findByUserNamePy(userNamePy, userId);
	}

	public List<? extends TreeVo> findNextLevelChildNodes(String treeId, String isCheckbox) {
		int temp = treeId.indexOf("_");
		int orgId = 0;
		if(temp > 0) {
			orgId = Integer.parseInt(treeId.substring(0, temp));
		}
		
		List<OrgRoleRef> orgRoleList = roleOrganService.findRolesByOrgId(orgId);
		List<TreeVoExtend> orgList = organService.findNextLevelChildNodesWithCheckbox(orgId, 0);
		
		for(TreeVoExtend treeVoExt : orgList) {
			treeVoExt.setNocheck(true); // 机构无checkbox
			treeVoExt.setId(treeVoExt.getId() + "_1"); 
		}
		
		for(OrgRoleRef orgRoleRef : orgRoleList) {
			Role role = orgRoleRef.getRole();
			TreeVoExtend treeVoExt = new TreeVoExtend();
			if(!Y.equals(isCheckbox)) {
				// 角色无checkbox
				treeVoExt.setNocheck(true);
				treeVoExt.setIconSkin(ROLE_CSS);
			} 
			treeVoExt.setId(orgRoleRef.getOrgRoleId() + "_2");
			treeVoExt.setText(role.getRoleName());
			treeVoExt.setLeaf(true);
			
			orgList.add(treeVoExt);
		}
		
		return orgList;
	}

	public Page<UserVo> findByPage(String treeId, String userName, String userCode, Pageable page) {
		List<Long> orgIdList = new ArrayList<Long>();
		if(!isNullOrEmpty(treeId) && treeId.length() >= 3) {
			int orgId = Integer.parseInt(treeId.substring(0, treeId.indexOf("_")));
			List<OrganVo> organVos = organService.findChildNodes(orgId, 0);
			for(OrganVo vo : organVos) {
				orgIdList.add(vo.getOrgId());
			}
		}
		Page<User> page2 = userMapper.findByPage2(treeId, orgIdList.toArray(new Long[orgIdList.size()]), userCode, userName, page);
		
		List<UserVo> vos = Lists.newArrayList();
		page2.getContent().forEach(entity -> {
			UserVo vo = new UserVo();
			BeanUtils.copyProperties(entity, vo);
			vos.add(vo);
		});
		
		return new PageImpl<>(vos, page, page2.getTotalElements());
	}

	public List<UserVo> findByOrgId(long organId) {
		List<User> list = userMapper.findByOrgId(organId);
		List<UserVo> vos = Lists.newArrayList();
		list.forEach(entity -> {
			UserVo vo = new UserVo();
			BeanUtils.copyProperties(entity, vo);
			vos.add(vo);
		});
		
		return vos;
	}

	public UserVo getUserByUserCodeAndPwd(String userCode, String password) throws ApplicationException {
		if(isNullOrEmpty(userCode) || isNullOrEmpty(userCode)) return null;
		try {
			User user = userMapper.getByUserCodeAndPwd(userCode, password);
			UserVo vo = new UserVo();
			BeanUtils.copyProperties(user, vo);
			return vo;
		} catch(Exception e) {
			logger.error("用户编码重复错误!", e);
			
			throw new ApplicationException("用户编码已存在错误!");
		}
	}

	public UserVo getUserByUserId(long userId) {
		User user = userMapper.get(userId);
		UserVo vo = new UserVo();
		BeanUtils.copyProperties(user, vo);
		
		List<RoleUserRefVo> roleUserVos = roleUserService.findByUserId(user.getUserId());
		for(RoleUserRefVo refVo : roleUserVos) {
			if(Y.equals(refVo.getIsDefault())) {
				long orgRoleId = refVo.getOrgRoleRef().getOrgRoleId();
				String roleName = refVo.getOrgRoleRef().getRole().getRoleName();
				
				vo.setOrgRoleId(orgRoleId);
				vo.setRoleName(roleName);
				break;
			}
		}
		
		return vo;
	}

	public List findByUserIds(Long[] userIds) {
		if(userIds == null || userIds.length == 0) return null;
		
		return userMapper.findByUserIds(userIds);
	}

	public Page<UserVo> findUsersForSetAdmin(String userName, String userCode, Pageable page) {
//		Map<String, Object> map = new WeakHashMap<String, Object>();
//		Page<User> page2 = userMapper.findByPage2("", null, userCode, userName, page);
//		List<User> userList = page2.getContent();
//		int count = (int) page2.getTotalElements();
//		List<User> systemAdminUserList = new ArrayList<User>();
//		SystemConfigXmlParse.getInstance();
//		List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
//		int systemAdminUserCount = 0;
//		if(userList != null && !userList.isEmpty()) {
//			for(User user : userList) {
//				for(String adminCode : systemAdminCode) {
//					if(user.getUserCode().equals(adminCode)) {
//						systemAdminUserList.add(user);
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
	
		Page<User> page2 = userMapper.findByPage2("", null, userCode, userName, page);
		List<UserVo> vos = Lists.newArrayList();
		SystemConfigXmlParse.getInstance();
		List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
		int count = 0;
		for (User user : page2.getContent()) {
			if (!systemAdminCode.contains(user.getUserCode())) {
				count++;
				UserVo vo = new UserVo();
				BeanUtils.copyProperties(user, vo);
				vos.add(vo);
			}	
		}
		
		return new PageImpl<>(vos, page, page2.getTotalElements() - count);
	}
	
	public Page<UserVo> findAdminUsers() {
		SystemConfigXmlParse.getInstance();
		List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
		List<UserVo> vos = new ArrayList<UserVo>();
		for(String userCode : systemAdminCode) {
			try {
				User entity = userMapper.getByUserCodeAndPwd(userCode, null);
				UserVo vo = new UserVo();
				BeanUtils.copyProperties(entity, vo);
				vos.add(vo);
			} catch(Exception e) {
				logger.warn("用户编码'{}'存在重复!", userCode);
			}
		}
		
		return new PageImpl<>(vos);
	}

	public boolean existsByUserCode(String userCode) {
		return userMapper.existsByUserCode(userCode);
	}

	public void update(UserVo user) throws ApplicationException {
		// 设置用户所属机构
		OrgRoleRef orgRoleRef = roleOrganService.get(user.getOrgRoleId() == null ? 0 : user.getOrgRoleId());
		user.setUserBelongtoOrg(orgRoleRef.getOrgan().getOrgId());
		user.setUserPassword(encrypt(user.getUserPassword())); // 加密
		
		userMapper.update(user); // 保存用户
		writeLog(user, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	public void updateBelongOrgIdAssignRole(long organId, long userId) {
		User user = new User();
		user.setUserId(userId);
		user.setUserBelongtoOrg(organId);
		userMapper.update(user);
	}
	
	public void updateUserInfo(User user) throws ApplicationException {
		User user2 = new User();
		user2.setUserId(user.getUserId());
		user2.setUserCode(user.getUserCode());
		user2.setUserName(user.getUserName());
		user2.setUserPassword(user.getUserPassword());
		userMapper.update(user2); // 保存用户
	}

	@Transactional(readOnly = false)
	public void updateUserPhotoUrl(String photoUrl, long userId) throws ApplicationException {
		User user = new User();
		user.setUserId(userId);
		user.setUserPhotoUrl(photoUrl);
		userMapper.update(user);
	}

	@Transactional(readOnly = false)
	public void resetPwdByUserId(String userDefaultPwd, String userName, long userId) 
			throws ApplicationException {
		
		User user = new User();
		user.setUserId(userId);
		user.setUserPassword(encrypt(userDefaultPwd));
		userMapper.update(user);
		
		String optContent = "重置密码用户：" + userName;
		sysOptLogService.saveLog(LogLevelEnum.SECOND, OptNameEnum.RESET_PWD, USER_MGR, optContent, userId + "");
	}

	public void save(UserVo user) throws ApplicationException {
		// 设置用户所属机构
		OrgRoleRef orgRoleRef = roleOrganService.get(user.getOrgRoleId());
		user.setUserBelongtoOrg(orgRoleRef.getOrgan().getOrgId());
		user.setUserPassword(encrypt(user.getUserPassword())); // 加密
		
		RoleUserRef roleUserRef = new RoleUserRef(); // 默认角色
		roleUserRef.setIsDefault(Y);
		roleUserRef.setOrgRoleRef(orgRoleRef);
		roleUserRef.setUser(user);
		
		roleUserService.save(roleUserRef); // 保存用户的默认角色
		userMapper.save(user);
		writeLog(user, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param user
	 * @param logLevel
	 * @param opt
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 4:17:57 PM
	 */
	private void writeLog(User user, LogLevelEnum logLevel, OptNameEnum opt) throws ApplicationException {
		StringBuilder optContent = new StringBuilder();
		optContent.append("用户编码：").append(user.getUserCode()).append(SPLIT);
		optContent.append("用户名称: ").append(user.getUserName()).append(SPLIT);
		optContent.append("所属机构: ").append(user.getUserBelongtoOrg()).append(SPLIT);
		
		sysOptLogService.saveLog(logLevel, opt, USER_MGR, optContent.toString(), user.getUserId() + "");
	}
	
}
