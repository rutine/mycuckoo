package com.mycuckoo.service.impl.uum;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumRoleRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.RoleService;

/**
 * 功能说明:  角色业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:31:44 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class RoleServiceImpl implements RoleService {
	
	static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	private UumRoleRepository roleRepository;
	private RoleOrganService roleOrganService;
	private PrivilegeService privilegeService;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public void disEnableRole(long roleId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			roleOrganService.deleteOrgRoleRefByRoleId(roleId, request); //根据角色ID删除机构角色关系记录，为停用角色所用
			privilegeService.deletePrivilegeByOwnerIdAType(roleId, OWNER_TYPE_ROL, request);  //删除用户所拥有操作、行权限
			UumRole uumRole = getRoleByRoleId(roleId);
			uumRole.setStatus(DISABLE);
			saveRole(uumRole, request);
			writeLog(uumRole, LOG_LEVEL_SECOND, DISABLE_DIS, request);
		} else {
			UumRole uumRole = getRoleByRoleId(roleId);
			uumRole.setStatus(ENABLE);
			saveRole(uumRole, request);
			writeLog(uumRole, LOG_LEVEL_SECOND, ENABLE_DIS, request);
		}
	}

	@Override
	public boolean existRoleByRoleName(String roleName) {
		int count = roleRepository.countRolesByRoleName(roleName);
		logger.debug("find Role total is " + count);
		if(count > 0) return true;
		return false;
	}

	@Override
	public List<UumRole> findAllRoles() {
		return roleRepository.findAllRoles();
	}

	@Override
	public Page<UumRole> findRoleByCon(String roleName, Pageable page) {
		logger.debug("start=" + page.getOffset() + " limit=" + page.getPageSize() + " roleName" + roleName);
		
		return roleRepository.findRolesByCon(roleName, page);
	}

	@Override
	public UumRole getRoleByRoleId(long roleId) {
		logger.debug("will find Role id is " + roleId);
		
		return roleRepository.get(roleId);
	}

	@Transactional(readOnly=false)
	@Override
	public void updateRole(UumRole uumRole, HttpServletRequest request) 
			throws ApplicationException {
		
		roleRepository.update(uumRole);
		writeLog(uumRole, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveRole(UumRole uumRole, HttpServletRequest request) throws ApplicationException {
		roleRepository.save(uumRole);
		writeLog(uumRole, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param uumRole 角色对象
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 7:39:34 PM
	 */
	private void writeLog(UumRole uumRole,String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = "角色名称 : " + uumRole.getRoleName() + SPLIT;
		sysOptLogService.saveLog(logLevel, ORGAN_MGR, opt, optContent, uumRole.getRoleId() + "", request);
	}
	
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setRoleRepository(UumRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
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

}
