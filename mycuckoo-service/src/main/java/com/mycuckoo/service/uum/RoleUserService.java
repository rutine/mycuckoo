package com.mycuckoo.service.uum;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.*;
import com.mycuckoo.repository.uum.RoleUserRefMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.uum.RoleUserRefVo;
import com.mycuckoo.vo.uum.RoleUserVo;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mycuckoo.common.constant.ServiceVariable.*;

/**
 * 功能说明: 角色用户业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 11:33:25 AM
 */
@Service
@Transactional(readOnly = true)
public class RoleUserService {
	static Logger logger = LoggerFactory.getLogger(RoleUserService.class);

	@Autowired
	private RoleUserRefMapper roleUserRefMapper;
	@Autowired
	private OrganRoleService roleOrganService;
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

	public void deleteByUserId(long userId) {
		roleUserRefMapper.deleteByUserId(userId);
	}

	public RoleUserRefVo findByUserIdAndOrgRoleId(long userId, long orgRoleId) {
		RoleUserRef entity = roleUserRefMapper.findByUserIdAndOrgRoleId(userId, orgRoleId);
		RoleUserRefVo vo = new RoleUserRefVo();
		BeanUtils.copyProperties(entity, vo);

		return vo;
	}

	public List<RoleUserVo> findByUserId(long userId) {
		List<RoleUserRef> roleUserRefList = roleUserRefMapper.findByUserId(userId);

		List<RoleUserVo> vos = Lists.newArrayList();
		for (RoleUserRef roleUserRef : roleUserRefList) {
			OrgRoleRef orgRoleRef = roleUserRef.getOrgRoleRef(); //机构角色关系
			Organ organ = orgRoleRef.getOrgan();
			Role role = orgRoleRef.getRole();
			User user = roleUserRef.getUser();

			RoleUserVo vo = new RoleUserVo();
			vo.setOrganRoleId(orgRoleRef.getOrgRoleId());
			vo.setOrganId(organ.getOrgId());
			vo.setOrganName(organ.getOrgSimpleName());
			vo.setRoleId(role.getRoleId());
			vo.setRoleName(role.getRoleName());
			vo.setUserId(user.getUserId());
			vo.setUserName(user.getUserName());
			vo.setUserPhotoUrl(user.getUserPhotoUrl());
			vo.setIsDefault(roleUserRef.getIsDefault());
			vos.add(vo);
		}

		return vos;
	}

	public List<RoleUserRefVo> findByOrgRoleId(Long orgId, Long roleId) {
		List<RoleUserRef> list = roleUserRefMapper.findByOrgRoleId(orgId, roleId);

		List<RoleUserRefVo> vos = Lists.newArrayList();
		list.forEach(entity -> {
			RoleUserRefVo vo = new RoleUserRefVo();
			BeanUtils.copyProperties(entity, vo);
			vos.add(vo);
		});

		return vos;
	}

	public void save(long userId, List<Long> orgRoleIds, long defaultOrgRoleId) {
		StringBuilder optContent = new StringBuilder();
		for (Long orgRoleId : orgRoleIds) {
			User user = new User(userId, null);
			OrgRoleRef orgRoleRef = new OrgRoleRef(orgRoleId);

			RoleUserRef roleUserRef = new RoleUserRef();
			roleUserRef.setUser(user);
			roleUserRef.setOrgRoleRef(orgRoleRef);
			if (defaultOrgRoleId == orgRoleId) {
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
				optContent.toString(), userId + "");
	}


	@Transactional
	public void save2(long userId, List<Long> orgRoleIds, long defaultOrgRoleId) {
		// 选择删除用户所有的角色
		this.deleteByUserId(userId);

		// 为用户添加角色
		this.save(userId, orgRoleIds, defaultOrgRoleId);
	}

	public void save(RoleUserRef roleUserRef) {
		roleUserRefMapper.save(roleUserRef);
	}

}
