package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ROL;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ORGAN_MGR;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.RoleMapper;
import com.mycuckoo.service.platform.SystemOptLogService;

/**
 * 功能说明:  角色业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:31:44 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class RoleService {
	static Logger logger = LoggerFactory.getLogger(RoleService.class);
	
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleOrganService roleOrganService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	public void disEnable(long roleId, String disEnableFlag) throws ApplicationException {
		if(DISABLE.equals(disEnableFlag)) {
			roleOrganService.deleteByRoleId(roleId); //根据角色ID删除机构角色关系记录，为停用角色所用
			privilegeService.deleteByOwnerIdAndOwnerType(roleId, OWNER_TYPE_ROL);  //删除用户所拥有操作、行权限
			
			Role role = getByRoleId(roleId);
			role.setStatus(DISABLE);
			roleMapper.save(role);
			writeLog(role, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
		} else {
			Role role = getByRoleId(roleId);
			role.setStatus(ENABLE);
			roleMapper.save(role);
			writeLog(role, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
		}
	}

	public boolean existByRoleName(String roleName) {
		int count = roleMapper.countByRoleName(roleName);
		
		logger.debug("find Role total is {}", count);
		
		return count > 0;
	}

	public List<Role> findAll() {
		return roleMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
	}

	public Page<Role> findByPage(String roleName, Pageable page) {
		logger.debug("start={} limit={} roleName={}", 
				page.getOffset(), page.getPageSize(), roleName);
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("roleName", CommonUtils.isNullOrEmpty(roleName) ? null : "%" + roleName + "%");
		
		return roleMapper.findByPage(params, page);
	}

	public Role getByRoleId(long roleId) {
		logger.debug("will find Role id is {}", roleId);
		
		return roleMapper.get(roleId);
	}

	@Transactional(readOnly=false)
	public void update(Role role) throws ApplicationException {
		roleMapper.update(role);
		
		writeLog(role, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	@Transactional(readOnly=false)
	public void save(Role role) throws ApplicationException {
		roleMapper.save(role);
		
		writeLog(role, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}
	
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param role 角色对象
	 * @param logLevel
	 * @param opt
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 7:39:34 PM
	 */
	private void writeLog(Role role,LogLevelEnum logLevel, OptNameEnum opt) throws ApplicationException {
		String optContent = "角色名称 : " + role.getRoleName() + SPLIT;
		sysOptLogService.saveLog(logLevel, opt, ORGAN_MGR, optContent, role.getRoleId() + "");
	}

}
