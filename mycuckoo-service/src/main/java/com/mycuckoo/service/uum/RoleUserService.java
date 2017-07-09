package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.ServiceVariable.N;
import static com.mycuckoo.common.constant.ServiceVariable.USER_ROLE_MGR;
import static com.mycuckoo.common.constant.ServiceVariable.Y;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.RoleUserRef;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.uum.RoleUserRefMapper;
import com.mycuckoo.service.platform.SystemOptLogService;

/**
 * 功能说明: 角色用户业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:33:25 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class RoleUserService {
	static Logger logger = LoggerFactory.getLogger(RoleUserService.class);
	
	@Autowired
	private RoleUserRefMapper roleUserRefMapper;
	@Autowired
	private RoleOrganService roleOrganService;
	@Autowired
	private UserService userService;
	@Autowired
	private SystemOptLogService sysOptLogService;


	public int countByOrgRoleId(long orgRoleId) {
		return roleUserRefMapper.countByOrgRoleId(orgRoleId);
	}

	public int countByRoleId(long roleId) {
		return roleUserRefMapper.countByRoleId(roleId);
	}

	public void deleteByUserId(long userId) throws ApplicationException {
		roleUserRefMapper.deleteByUserId(userId);
	}

	public RoleUserRef findByUserIdAndOrgRoleId(long userId, long orgRoleId) {
		return roleUserRefMapper.findByUserIdAndOrgRoleId(userId, orgRoleId);
	}

	public List<RoleUserRef> findByUserId(long userId) {
		List<RoleUserRef> roleUserRefList = roleUserRefMapper.findByUserId(userId);
		for(RoleUserRef roleUserRef : roleUserRefList) {
			OrgRoleRef orgRoleRef = roleUserRef.getOrgRoleRef(); //机构角色关系
			Organ organ = orgRoleRef.getOrgan();
			Role role = orgRoleRef.getRole();
			
			roleUserRef.setOrgRoleId(orgRoleRef.getOrgRoleId());
			roleUserRef.setOrganId(organ.getOrgId());
			roleUserRef.setOrganName(organ.getOrgSimpleName());
			roleUserRef.setRoleId(role.getRoleId());
			roleUserRef.setRoleName(role.getRoleName());
		}
		
		return roleUserRefList;
	}
	
	public List<RoleUserRef> findByOrgRoleId(Long orgId, Long roleId) {
		return roleUserRefMapper.findByOrgRoleId(orgId, roleId);
	}

	public void save(long userId, List<Long> orgRoleIds, long defaultOrgRoleId) 
			throws ApplicationException {
		
		StringBuilder optContent = new StringBuilder();
		for(Long orgRoleId : orgRoleIds) {
			User user = new User(userId, null);
			OrgRoleRef orgRoleRef = new OrgRoleRef(orgRoleId);
			
			RoleUserRef roleUserRef = new RoleUserRef();
			roleUserRef.setUser(user);
			roleUserRef.setOrgRoleRef(orgRoleRef);
			if(defaultOrgRoleId == orgRoleId) {
				roleUserRef.setIsDefault(Y);
				OrgRoleRef orgRoleReff = roleOrganService.get(orgRoleId);
				//修改用户的默认机构
				userService.updateBelongOrgIdAssignRole(orgRoleReff.getOrgan().getOrgId(), userId);
			} else {
				roleUserRef.setIsDefault(N);
			}
			roleUserRefMapper.save(roleUserRef);
			optContent.append(optContent.length() > 0 ? ", " + orgRoleId : orgRoleId);
		}
		
		sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, USER_ROLE_MGR, 
				optContent.toString(), userId+"");
	}

	
	@Transactional(readOnly=false)
	public void save2(long userId, List<Long> orgRoleIds, long defaultOrgRoleId) throws ApplicationException {
		// 选择删除用户所有的角色
		this.deleteByUserId(userId);
		
		// 为用户添加角色
		this.save(userId, orgRoleIds, defaultOrgRoleId);
	}

	public void save(RoleUserRef roleUserRef) throws ApplicationException {
		roleUserRefMapper.save(roleUserRef);
	}

}
