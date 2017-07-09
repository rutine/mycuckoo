package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ROL;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.USER_AGENT;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.ModuleLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.constant.PrivilegeScopeEnum;
import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.ModuleMemu;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserAgent;
import com.mycuckoo.domain.uum.UserAgentPrivilege;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.UserAgentMapper;
import com.mycuckoo.repository.uum.UserAgentPrivilegeMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.platform.ModuleMemuVo;
import com.mycuckoo.vo.uum.RoleUserRefVo;
import com.mycuckoo.vo.uum.UserAgentVo;

/**
 * 功能说明: 用户代理业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:34:45 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserAgentService {
	static Logger logger = LoggerFactory.getLogger(UserAgentService.class);
	
	@Autowired
	private UserAgentMapper userAgentMapper;
	@Autowired
	private UserAgentPrivilegeMapper userAgentPrivilegeMapper;
	
	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private RoleOrganService roleOrganService;
	@Autowired
	private SystemOptLogService sysOptLogService;
	@Autowired
	private PlatformServiceFacade platformServiceFacade;
	

	public void deleteUserAgentsByUserId(long userId) throws ApplicationException {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		List<UserAgent> userAgentList = userAgentMapper
				.findByPage(params, new PageRequest(0, Integer.MAX_VALUE)).getContent();
		
		userAgentList.forEach(agent -> {
			userAgentMapper.delete(agent.getAgentId());
		});
	}

	public void deleteByAgentIds(List<Long> agentIds) throws ApplicationException {
		agentIds.forEach(agentId -> {
			userAgentMapper.delete(agentId);
		});
	}

	public void deleteUserAgentsByOwnerId(Long ownerId, String ownerType) {
		List<UserAgent> userAgentList = null;
		if(OWNER_TYPE_ROL.equals(ownerType)) {
			List<Long> orgRoleIdList = roleOrganService.findOrgRoleRefIdsByRoleId(ownerId);
			if(orgRoleIdList == null) return;
			userAgentList = userAgentMapper
					.findByOwnerId(orgRoleIdList.toArray(new Long[orgRoleIdList.size()]));
		} else if(OWNER_TYPE_USR.equals(ownerType)) {
			Map<String, Object> params = Maps.newHashMap();
			params.put("userId", ownerId);
			userAgentList = userAgentMapper
					.findByPage(params, new PageRequest(0, Integer.MAX_VALUE)).getContent();
		}
		
		userAgentList.forEach(userAgent -> {
			userAgentMapper.delete(userAgent.getAgentId());
		});
	}

	public List<String> findPrivilegeIdsByAgentId(Long agentId) {
		return userAgentPrivilegeMapper.findPrivilegeIdsByAgentId(agentId);
	}

	public List<TreeVo> findAgentPrivilegeByAgentIdForAgentUser(Long agentId) {
		List list = userAgentPrivilegeMapper.findPrivilegeIdsByAgentId(agentId);
		if(list.size() == 1) {
			String privilegeId = (String) list.get(0);
			if(PrivilegeScopeEnum.ALL.value().equals(privilegeId)) return null;
		}
		
		// 查找所有操作
		List<ModOptRef> allModOptRefList = platformServiceFacade.findAllModOptRefs();
		List<ModOptRef> privilegeList = new ArrayList<ModOptRef>();
		for(int i = 0, len = list.size(); i < len; i++) {
			String privilegeId = (String) list.get(i);
			for(ModOptRef modOptRef : allModOptRefList) {
				long modOptId = modOptRef.getModOptId();
				if(modOptId == Long.parseLong(privilegeId)) {
					privilegeList.add(modOptRef);
					break;
				}
			}
		}
		
		List<ModuleMemuVo> assignedModMenuList = privilegeService.filterModOpt(privilegeList, true).getModuleMenu();
		
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if (assignedModMenuList != null && assignedModMenuList.size() > 0) {
			for (ModuleMemu mod : assignedModMenuList) {
				TreeVo treeVo = new TreeVo();
				treeVo.setId(mod.getModuleId().toString());
				treeVo.setText(mod.getModName());
				treeVo.setIconSkin(mod.getModImgCls());
				if (ModuleLevelEnum.THREE.value().equals(mod.getModLevel())) {
					treeVo.setLeaf(true); // 模块菜单级别为3是叶子
				} else {
					treeVo.setIsParent(true);
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	public List<UserAgentVo> findByAgentUserId(long agentUserId) {
		List<UserAgent> userAgentList = userAgentMapper.findByAgentUserId(agentUserId, 
				Calendar.getInstance().getTime());
		
		List<UserAgentVo> vos = Lists.newArrayList();
		for(UserAgent userAgent : userAgentList) {
			UserAgentVo vo = new UserAgentVo();
			BeanUtils.copyProperties(userAgent, vo);
			
			long userId = userAgent.getUserId(); // 被代理用户ID
			long orgRoleId = userAgent.getOrgRoleId(); // 被代理用户机构角色ID
			
			RoleUserRefVo roleUserRef = roleUserService.findByUserIdAndOrgRoleId(userId, orgRoleId);
			User user = userService.getUserByUserId(userId);
			vo.setUserId(user.getUserId());
			vo.setUserCode(user.getUserCode());
			vo.setUserName(user.getUserName());
			vo.setUserPhotoUrl(user.getUserPhotoUrl());
			
			OrgRoleRef orgRoleRef = roleUserRef.getOrgRoleRef();
			Role uumRole = orgRoleRef.getRole();
			vo.setRoleId(uumRole.getRoleId());
			vo.setRoleName(uumRole.getRoleName());
			
			Organ organ = orgRoleRef.getOrgan();
			vo.setOrganId(organ.getOrgId());
			vo.setOrganName(organ.getOrgSimpleName());
			
			vos.add(vo);
		}
	
		
		return vos;
	}

	public Page<UserAgentVo> findUserAgentByCon(long userId, Pageable page) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		Page<UserAgent> page2 = userAgentMapper.findByPage(params, page);
		
		List<UserAgentVo> vos = Lists.newArrayList();
		for(UserAgent userAgent : page2.getContent()) {
			UserAgentVo vo = new UserAgentVo();
			BeanUtils.copyProperties(userAgent, vo);
			
			Long agentUserId = userAgent.getAgentUserId();
			User user = userService.getUserByUserId(agentUserId);
			vo.setUserName(user.getUserName());
			vos.add(vo);
		}
		
		return new PageImpl<>(vos, page, page2.getTotalElements());
	}

	public boolean existsAgentUser(long userId, long userAgentId) {
		int count = userAgentMapper.countByUserIdAndAgentUserId(userId, userAgentId);
		
		return count > 0;
	}

	public void updateUserAgent(UserAgent userAgent) throws ApplicationException {
		userAgentMapper.update(userAgent);
		
		writeLog(userAgent, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	public void save(UserAgent userAgent, List<String> modOptIdList) throws ApplicationException {
		if(modOptIdList != null) {
			for(String modOptId : modOptIdList) {
				UserAgentPrivilege userAgentPrivilege = new UserAgentPrivilege();
				userAgentPrivilege.setUserAgent(userAgent);
				userAgentPrivilege.setPrivilegeId(modOptId);
				userAgentPrivilege.setPrivilegeType(PRIVILEGE_TYPE_OPT);
				userAgentPrivilege.setUserAgent(userAgent);
				userAgentPrivilegeMapper.save(userAgentPrivilege); // 代理权限
			}
		}
		userAgentMapper.save(userAgent); // 代理记录
		
		writeLog(userAgent, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param userAgent
	 * @param logLevel
	 * @param opt
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 19, 2012 11:01:10 PM
	 */
	private void writeLog(UserAgent userAgent, LogLevelEnum logLevel, 
			OptNameEnum opt) throws ApplicationException {
		StringBuilder optContent = new StringBuilder();
		optContent.append("代理ID：").append(userAgent.getAgentId()).append(SPLIT);
		optContent.append("用户ID: ").append(userAgent.getUserId()).append(SPLIT);
		optContent.append("代理用户ID: ").append(userAgent.getAgentUserId()).append(SPLIT);
		
		sysOptLogService.saveLog(logLevel, opt, USER_AGENT, 
				optContent.toString(), userAgent.getAgentId() + "");
	}
}
