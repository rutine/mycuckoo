package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.ROLE_ASSIGN;

import java.util.ArrayList;
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
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.OrgRoleRefMapper;
import com.mycuckoo.service.platform.SystemOptLogService;

/**
 * 功能说明: 机构角色业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:29:55 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class OrganRoleService {
	static Logger logger = LoggerFactory.getLogger(OrganRoleService.class);
	
	@Autowired
	private OrgRoleRefMapper orgRoleRefMapper;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleUserService roleUserService; 
	@Autowired
	private SystemOptLogService sysOptLogService;

	
	public int countByOrgId(long orgId) {
		return orgRoleRefMapper.countByOrgId(orgId);
	}

	@Transactional(readOnly=false)
	public boolean deleteOrgRoleRef(long orgId, List<Long> roleIds) throws ApplicationException {
		List<OrgRoleRef> orgRoleRefList = new ArrayList<OrgRoleRef>();
		StringBuilder optContent = new StringBuilder("删除机构下的角色ID：");
		for (long roleId : roleIds) {
			OrgRoleRef orgRoleRef = getByOrgRoleId(orgId, roleId);
			int roleUserCount = roleUserService.countByOrgRoleId(orgRoleRef.getOrgRoleId());
			if (roleUserCount == 0) {// 角色下无用户
				orgRoleRefMapper.delete(orgRoleRef.getOrgRoleId()); //删除所有机构角色
			} else {// 角色下有用户
				return true;
			}
			optContent.append(roleId + SPLIT);
		}
		
		sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.DELETE, ROLE_ASSIGN, 
				optContent.toString(), orgId+"");
		
		return false;
	}

	@Transactional(readOnly=false)
	public void deleteByRoleId(long roleId) {
		orgRoleRefMapper.deleteByRoleId(roleId);
	}

	public OrgRoleRef getByOrgRoleId(long orgId, long roleId) throws ApplicationException { 
		return orgRoleRefMapper.getByOrgRoleId(orgId, roleId);
	}

	public OrgRoleRef get(long orgRoleId) { 
		return orgRoleRefMapper.get(orgRoleId);
	}

	public List<OrgRoleRef> findByOrgRoleIds(Long[] orgRoleRefIds) { 
		return orgRoleRefMapper.findByOrgRoleIds(orgRoleRefIds);
	}

	public List<Long> findOrgRoleRefIdsByRoleId(Long roleId) { 
		return orgRoleRefMapper.findOrgRoleIdsByRoleId(roleId);
	}

	public List<OrgRoleRef> findRolesByOrgId(long orgId) { 
		return orgRoleRefMapper.findRolesByOrgId(orgId);
	}

	public Page<Role> findSelectedRolesByOrgId(long orgId, String roleName, Pageable page) { 
		Page<OrgRoleRef> pageTemp = orgRoleRefMapper.findRolesByPage(orgId, roleName, page);
		List<Role> roleList = new ArrayList<Role>();
		for(OrgRoleRef orgRoleRef : pageTemp.getContent()) {
			roleList.add(orgRoleRef.getRole());
		}
		
		return new PageImpl<Role>(roleList, page, pageTemp.getTotalElements());
	}

	public Page<Role> findUnselectedRolesByOrgId(long orgId, Pageable page) { 
		List<Role> allRoleList = roleService.findAll();
		List<Role> selectedRoleList = findSelectedRoleByOrgId(orgId);
		allRoleList.removeAll(selectedRoleList);
		List<Role> newRoleList = new ArrayList<Role>();
		int count = 0;
		for (Role role : allRoleList) {
			count++;
			if (count >= (page.getOffset() + 1) && count <= (page.getPageSize() + page.getOffset())) {
				newRoleList.add(role);
			}
		}
		
		return new PageImpl<Role>(newRoleList, page, count);
	}

	@Transactional(readOnly=false)
	public void save(long orgId, List<Long> roleIds) throws ApplicationException {
		if(!roleIds.isEmpty()) {
			StringBuilder optContent = new StringBuilder(); 
			Organ organ = new Organ();
			organ.setOrgId(orgId);
			for(Long roleId : roleIds) {
				Role role = new Role();
				role.setRoleId(roleId);
				
				OrgRoleRef orgRoleRef = new OrgRoleRef();
				orgRoleRef.setOrgan(organ);
				orgRoleRef.setRole(role);
				orgRoleRefMapper.save(orgRoleRef);
				
				optContent.append("机构id:" + orgId + SPLIT + "角色id:" + roleId + SPLIT);
			}
			
			sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, ROLE_ASSIGN, 
					optContent.toString(), "");
		}
	}


	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 根据机构ID查询其下已有的角色信息
	 *
	 * @param orgId
	 * @return
	 * @author rutine
	 * @time Oct 18, 2012 8:44:47 PM
	 */
	private List<Role> findSelectedRoleByOrgId(long orgId) {
		List<OrgRoleRef> orgRoleRefList = orgRoleRefMapper.findRolesByOrgId(orgId);
		List<Role> roleList = new ArrayList<Role>();
		if (!orgRoleRefList.isEmpty()) {
			for (OrgRoleRef orgRoleRef : orgRoleRefList) {
				roleList.add(orgRoleRef.getRole());
			}
		}
		
		return roleList;
	}

}
