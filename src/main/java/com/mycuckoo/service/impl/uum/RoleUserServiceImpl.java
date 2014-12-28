package com.mycuckoo.service.impl.uum;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.ServiceVariable.N;
import static com.mycuckoo.common.constant.ServiceVariable.USER_ROLE_MGR;
import static com.mycuckoo.common.constant.ServiceVariable.Y;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumRoleUserRefRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 角色用户业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:33:25 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class RoleUserServiceImpl implements RoleUserService {
	
	static Logger logger = LoggerFactory.getLogger(RoleUserServiceImpl.class);
	private UumRoleUserRefRepository roleUserRefRepository;
	private RoleOrganService roleOrganService;
	private UserService userService;
	private SystemOptLogService sysOptLogService;

	@Override
	public int countRoleUserRefsByOrgRoleId(long orgRoleId) {
		return roleUserRefRepository.countRoleUserRefsByOrgRoleId(orgRoleId);
	}

	@Override
	public int countRoleUserRefsByRoleId(long roleId) {
		return roleUserRefRepository.countRoleUserRefsByRoleId(roleId);
	}

	@Override
	public void deleteRoleUserRefByUserId(long userId, HttpServletRequest request) throws ApplicationException {
		roleUserRefRepository.deleteRoleUserRefByUserId(userId);
	}

	@Override
	public UumRoleUserRef getRoleUserRefByUserIdAOrgRoleId(long userId, long orgRoleId) {
		return roleUserRefRepository.getRoleUserRefByUserIdAOrgRoleId(userId, orgRoleId);
	}

	@Override
	public List<UumRoleUserRef> findRoleUserRefsByUserId(long userId) {
		List<UumRoleUserRef> roleUserRefList = roleUserRefRepository.findRoleUserRefsByUserId(userId);
//		if(roleUserRefList == null || roleUserRefList.isEmpty()) return null;
		for(UumRoleUserRef uumRoleUserRef : roleUserRefList) {
			UumOrgRoleRef uumOrgRoleRef = uumRoleUserRef.getUumOrgRoleRef(); //机构角色关系
			UumOrgan uumOrgan = uumOrgRoleRef.getUumOrgan();
			UumRole uumRole = uumOrgRoleRef.getUumRole();
			
			uumRoleUserRef.setOrgRoleId(uumOrgRoleRef.getOrgRoleId());
			uumRoleUserRef.setOrganId(uumOrgan.getOrgId());
			uumRoleUserRef.setOrganName(uumOrgan.getOrgSimpleName());
			uumRoleUserRef.setRoleId(uumRole.getRoleId());
			uumRoleUserRef.setRoleName(uumRole.getRoleName());
		}
		
		return roleUserRefList;
	}
	
	@Override
	public List<UumRoleUserRef> findRoleUserRefsByOrgRoleId(Long orgId, Long roleId) {
		return roleUserRefRepository.findRoleUserRefsByOrgRoleId(orgId, roleId);
	}

	@Override
	public void saveRoleUserRef(long userId, List<Long> orgRoleIds, long defaultOrgRoleId, 
			HttpServletRequest request) throws ApplicationException {
		
		StringBuilder optContent = new StringBuilder();
		List<UumRoleUserRef> roleUserRefList = new ArrayList<UumRoleUserRef>();
		for(Long orgRoleId : orgRoleIds) {
			UumUser uumUser = new UumUser(userId, null);
			UumOrgRoleRef uumOrgRoleRef = new UumOrgRoleRef(orgRoleId);
			
			UumRoleUserRef uumRoleUserRef = new UumRoleUserRef();
			uumRoleUserRef.setUumUser(uumUser);
			uumRoleUserRef.setUumOrgRoleRef(uumOrgRoleRef);
			if(defaultOrgRoleId == orgRoleId) {
				uumRoleUserRef.setIsDefault(Y);
				UumOrgRoleRef uumOrgRoleReff = roleOrganService.getOrgRoleRefById(orgRoleId);
				//修改用户的默认机构
				userService.updateUserBelongOrgIdAssignRole(uumOrgRoleReff.getUumOrgan().getOrgId(), userId, request);
			} else {
				uumRoleUserRef.setIsDefault(N);
			}
			roleUserRefList.add(uumRoleUserRef);
			optContent.append(optContent.length() > 0 ? ", " + orgRoleId : orgRoleId);
		}
		
		roleUserRefRepository.save(roleUserRefList);
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, USER_ROLE_MGR,SAVE_OPT, optContent.toString(), userId+"", request);
	}

	
	@Transactional(readOnly=false)
	@Override
	public void saveRoleUserRef2(long userId, List<Long> orgRoleIds, long defaultOrgRoleId, 
			HttpServletRequest request) throws ApplicationException {
		
		// 选择删除用户所有的角色
		this.deleteRoleUserRefByUserId(userId, request);
		
		// 为用户添加角色
		this.saveRoleUserRef(userId, orgRoleIds, defaultOrgRoleId, request);
	}

	@Override
	public void saveRoleUserRef(UumRoleUserRef uumRoleUserRef) throws ApplicationException {
		roleUserRefRepository.save(uumRoleUserRef);
	}


	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setRoleUserRefRepository(
		UumRoleUserRefRepository roleUserRefRepository) {
		this.roleUserRefRepository = roleUserRefRepository;
	}
	@Autowired
	public void setRoleOrganService(RoleOrganService roleOrganService) {
		this.roleOrganService = roleOrganService;
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
