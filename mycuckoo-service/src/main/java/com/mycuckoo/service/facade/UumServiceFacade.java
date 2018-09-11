package com.mycuckoo.service.facade;

import com.mycuckoo.domain.uum.User;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.UserOrgRoleService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.TreeVoExtend;
import com.mycuckoo.vo.platform.ModuleMemuVo;
import com.mycuckoo.vo.uum.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能说明: 用户常用业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:15:12 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class UumServiceFacade {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserOrgRoleService roleUserService;
	@Autowired
	private PrivilegeService privilegeService;
	

	public void deletePrivilegeByModOptId(String[] modOptRefIds) {
		this.privilegeService.deletePrivilegeByModOptId(modOptRefIds);
	}



	public List<UserRoleVo> findRoleUsersByUserId(long userId) {
		return roleUserService.findByUserId(userId);
	}

	public User getUserByUserCodeAndPwd(String userCode, String password) {
		return userService.getUserByUserCodeAndPwd(userCode, password);
	}

	public HierarchyModuleVo findPrivilegesForAdminLogin() {
		return privilegeService.findPrivilegesForAdminLogin();
	}

	public HierarchyModuleVo findPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
		return privilegeService.findPrivilegesForUserLogin(userId, roleId, organId, organRoleId);
	}

	public boolean existsSpecialPrivilegeForUser(long userId) {
		return privilegeService.existsSpecialPrivilegeByUserId(userId);
	}

	public List<TreeVoExtend> convertToTree(List<ModuleMemuVo> vos) {
		return privilegeService.convertToTree(vos);
	}
}
