package com.mycuckoo.service.impl.uum;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ROL;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_ALL;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.THREE;
import static com.mycuckoo.common.constant.ServiceVariable.USER_AGENT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.domain.uum.UumUserAgentPrivilege;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumUserAgentPrivilegeRepository;
import com.mycuckoo.persistence.iface.uum.UumUserAgentRepository;
import com.mycuckoo.service.iface.commonused.PlatformCommonService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserAgentService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 用户代理业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:34:45 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserAgentServiceImpl implements UserAgentService {
	
	static Logger logger = LoggerFactory.getLogger(UserAgentServiceImpl.class);
	private UumUserAgentRepository userAgentRepository;
	private UumUserAgentPrivilegeRepository userAgentPrivilegeRepository;
	private UserService userService;
	private PrivilegeService privilegeService;
	private RoleUserService roleUserService;
	private RoleOrganService roleOrganService;
	private SystemOptLogService sysOptLogService;
	private PlatformCommonService platformCommonService;

	@Override
	public void deleteUserAgentsByUserId(long userId, HttpServletRequest request) 
			throws ApplicationException {
		
		List<UumUserAgent> userAgentList = userAgentRepository.findUserAgentsByUserId(userId);
		userAgentRepository.delete(userAgentList);
	}

	@Override
	public void deleteUserAgentsByAgentIds(List<Long> agentIds, HttpServletRequest request) 
			throws ApplicationException {
		
		List<UumUserAgent> userAgentList = new ArrayList<UumUserAgent>();
		for(Long agentId : agentIds) {
			UumUserAgent uumUserAgent = findUserAgentByAgentId(agentId);
			userAgentList.add(uumUserAgent);
		}
		
		userAgentRepository.delete(userAgentList);
	}

	@Override
	public void deleteUserAgentsByOwnerId(Long ownerId, String ownerType, HttpServletRequest request) {
		
		List<UumUserAgent> userAgentList = null;
		if(OWNER_TYPE_ROL.equals(ownerType)) {
			List<Long> orgRoleIdList = roleOrganService.findOrgRoleRefIdsByRoleId(ownerId);
			if(orgRoleIdList == null) return;
			userAgentList = userAgentRepository
					.findUserAgentsByOwnerId(orgRoleIdList.toArray(new Long[orgRoleIdList.size()]));
		} else if(OWNER_TYPE_USR.equals(ownerType)) {
			userAgentList = userAgentRepository.findUserAgentsByUserId(ownerId);
		}
		
		userAgentRepository.delete(userAgentList);
	}

	@Override
	public List<String> findPrivilegeIdsByAgentId(Long agentId) {
		return userAgentPrivilegeRepository.findPrivilegeIdsByAgentId(agentId);
	}

	@Override
	public List<TreeVo> findAgentPrivilegeForAgentUserByAgentId(Long agentId) {
		List list = userAgentPrivilegeRepository.findPrivilegeIdsByAgentId(agentId);
		if(list.size() == 1) {
			String privilegeId = (String) list.get(0);
			if(PRIVILEGE_SCOPE_ALL.equals(privilegeId)) return null;
		}
		// 查找所有操作
		List<SysplModOptRef> allModOptRefList = platformCommonService.findAllModOptRefs();
		List<SysplModOptRef> privilegeList = new ArrayList<SysplModOptRef>();
		for(int i = 0, len = list.size(); i < len; i++) {
			String privilegeId = (String) list.get(i);
			for(SysplModOptRef modOptRef : allModOptRefList) {
				long modOptId = modOptRef.getModOptId();
				if(modOptId == Long.parseLong(privilegeId)) {
					privilegeList.add(modOptRef);
					break;
				}
			}
		}
		
		List<SysplModuleMemu> assignedModMenuList = 
				(List<SysplModuleMemu>) privilegeService.filterModOpt(privilegeList, true).get("moduleMenu");
		
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if (assignedModMenuList != null && assignedModMenuList.size() > 0) {
			for (SysplModuleMemu mod : assignedModMenuList) {
				TreeVo treeVo = new TreeVo();
				treeVo.setId(mod.getModuleId().toString());
				treeVo.setText(mod.getModName());
				treeVo.setIconSkin(mod.getModImgCls());
				if (THREE.equals(mod.getModLevel())) {
					treeVo.setLeaf(true); // 模块菜单级别为3是叶子
				} else {
					treeVo.setIsParent(true);
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	@Override
	public List<UumUserAgent> findUserAgentsByAgentUserId(long agentUserId) {
		List<UumUserAgent> userAgentList = userAgentRepository.findUserAgentsByAgentUserId(agentUserId);
		if(userAgentList != null) {
			for(UumUserAgent userAgent : userAgentList) {
				long userId = userAgent.getUserId(); // 被代理用户ID
				long orgRoleId = userAgent.getOrgRoleId(); // 被代理用户机构角色ID
				
				UumRoleUserRef uumRoleUserRef = roleUserService.getRoleUserRefByUserIdAOrgRoleId(userId, orgRoleId);
				UumUser uumUser = userService.getUserByUserId(userId);
				userAgent.setUserId(uumUser.getUserId());
				userAgent.setUserCode(uumUser.getUserCode());
				userAgent.setUserName(uumUser.getUserName());
				userAgent.setUserPhotoUrl(uumUser.getUserPhotoUrl());
				
				UumOrgRoleRef uumOrgRoleRef = uumRoleUserRef.getUumOrgRoleRef();
				UumRole uumRole = uumOrgRoleRef.getUumRole();
				userAgent.setRoleId(uumRole.getRoleId());
				userAgent.setRoleName(uumRole.getRoleName());
				
				UumOrgan uumOrgan = uumOrgRoleRef.getUumOrgan();
				userAgent.setOrganId(uumOrgan.getOrgId());
				userAgent.setOrganName(uumOrgan.getOrgSimpleName());
			}
		}
		
		return userAgentList;
	}

	@Override
	public Page<UumUserAgent> findUserAgentByCon(long userId, Pageable page) {
		Page<UumUserAgent> page2 = userAgentRepository.findUserAgentsByCon(userId, page);
		if(page2.getContent() != null) {
			for(UumUserAgent uumUserAgent : page2.getContent()) {
				Long agentUserId = uumUserAgent.getAgentUserId();
				UumUser uumUser = userService.getUserByUserId(agentUserId);
				uumUserAgent.setUserName(uumUser.getUserName());
			}
		}
		
		return page2;
	}

	@Override
	public boolean existsAgentUser(long userId, long userAgentId) {
		int count = userAgentRepository.countUserAgentsByUserIdAAgentUserId(userId, userAgentId);
		if(count > 0) return true;
		return false;
	}

	@Override
	public void updateUserAgent(UumUserAgent uumUserAgent, HttpServletRequest request) 
			throws ApplicationException {
		
		userAgentRepository.update(uumUserAgent);
		writeLog(uumUserAgent, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Override
	public void saveUserAgent(UumUserAgent uumUserAgent, List<String> modOptIdList, 
			HttpServletRequest request) throws ApplicationException {
		
		if(modOptIdList != null) {
			Set<UumUserAgentPrivilege> uumUserAgentPrivilegeSet = new HashSet<UumUserAgentPrivilege>();
			for(String modOptId : modOptIdList) {
				UumUserAgentPrivilege uumUserAgentPrivilege = new UumUserAgentPrivilege();
				uumUserAgentPrivilege.setUumUserAgent(uumUserAgent);
				uumUserAgentPrivilege.setPrivilegeId(modOptId);
				uumUserAgentPrivilege.setPrivilegeType(PRIVILEGE_TYPE_OPT);
				uumUserAgentPrivilege.setUumUserAgent(uumUserAgent);
				
				uumUserAgentPrivilegeSet.add(uumUserAgentPrivilege);
			}
			userAgentPrivilegeRepository.save(uumUserAgentPrivilegeSet); // 代理权限
		}
		userAgentRepository.save(uumUserAgent); // 代理记录
		
		writeLog(uumUserAgent, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param uumUserAgent
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 19, 2012 11:01:10 PM
	 */
	private void writeLog(UumUserAgent uumUserAgent, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = uumUserAgent.getAgentId() + SPLIT 
			+ uumUserAgent.getUserId() + SPLIT 
			+ uumUserAgent.getAgentUserId() + SPLIT;
		sysOptLogService.saveLog(logLevel, USER_AGENT, opt, optContent, uumUserAgent.getAgentId() + "", request);
	}
	/**
	 * 根据代理ID查询用户代理 
	 *
	 * @param AgentId 代理ID
	 * @return UumUserAgent 用户代理对象
	 * @author rutine
	 * @time Oct 20, 2012 8:05:30 AM
	 */
	private UumUserAgent findUserAgentByAgentId(Long AgentId) {
		return userAgentRepository.get(AgentId);
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setUserAgentRepository(UumUserAgentRepository userAgentRepository) {
		this.userAgentRepository = userAgentRepository;
	}
	@Autowired
	public void setUserAgentPrivilegeRepository(UumUserAgentPrivilegeRepository userAgentPrivilegeRepository) {
		this.userAgentPrivilegeRepository = userAgentPrivilegeRepository;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}
	@Autowired
	public void setRoleUserService(RoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}
	@Autowired
	public void setRoleOrganService(RoleOrganService roleOrganService) {
		this.roleOrganService = roleOrganService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	@Autowired
	public void setPlatformCommonService(PlatformCommonService platformCommonService) {
		this.platformCommonService = platformCommonService;
	}

}
