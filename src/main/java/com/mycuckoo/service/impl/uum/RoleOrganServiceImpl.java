package com.mycuckoo.service.impl.uum;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumOrgRoleRefRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.RoleService;
import com.mycuckoo.service.iface.uum.RoleUserService;

/**
 * 功能说明: 机构角色业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:29:55 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class RoleOrganServiceImpl implements RoleOrganService {
	
	static Logger logger = LoggerFactory.getLogger(RoleOrganServiceImpl.class);
	private UumOrgRoleRefRepository orgRoleRefRepository;
	private RoleService roleService;
	private RoleUserService roleUserService; 
	private SystemOptLogService sysOptLogService;

	@Override
	public int countOrgRoleRefsByOrgId(long orgId) {
		return orgRoleRefRepository.countOrgRoleRefsByOrgId(orgId);
	}

	@Transactional(readOnly=false)
	@Override
	public boolean deleteOrgRoleRef(long orgId, List<Long> roleIds,  HttpServletRequest request) throws ApplicationException {
		List<UumOrgRoleRef> orgRoleRefList = new ArrayList<UumOrgRoleRef>();
		StringBuilder optContent = new StringBuilder("删除机构下的角色ID：");
		for (long roleId : roleIds) {
			UumOrgRoleRef uumOrgRoleRef = getOrgRoleRefByOrgRoleId(orgId, roleId);
			int roleUserCount = roleUserService.countRoleUserRefsByOrgRoleId(uumOrgRoleRef.getOrgRoleId());
			if (roleUserCount == 0) {// 角色下无用户
				orgRoleRefList.add(uumOrgRoleRef);
			} else {// 角色下有用户
				return true;
			}
			optContent.append(roleId + SPLIT);
		}
		orgRoleRefRepository.delete(orgRoleRefList); //删除所有机构角色
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, ROLE_ASSIGN, DELETE_OPT, optContent.toString(), orgId+"", request);
		
		return false;
	}

	@Transactional(readOnly=false)
	@Override
	public void deleteOrgRoleRefByRoleId(long roleId, HttpServletRequest request) {
		orgRoleRefRepository.deleteOrgRoleRefsByRoleId(roleId);
	}

	@Override
	public UumOrgRoleRef getOrgRoleRefByOrgRoleId(long orgId, long roleId) throws ApplicationException { 
		return orgRoleRefRepository.getOrgRoleRefByOrgRoleId(orgId, roleId);
	}

	@Override
	public UumOrgRoleRef getOrgRoleRefById(long orgRoleId) { 
		return orgRoleRefRepository.get(orgRoleId);
	}

	@Override
	public List<UumOrgRoleRef> findOrgRoleRefByOrgRoleIds(Long[] orgRoleRefIds) { 
		return orgRoleRefRepository.findOrgRoleRefsByOrgRoleIds(orgRoleRefIds);
	}

	@Override
	public List<Long> findOrgRoleRefIdsByRoleId(Long roleId) { 
		return orgRoleRefRepository.findOrgRoleIdsByRoleId(roleId);
	}

	@Override
	public List<UumOrgRoleRef> findRolesByOrgId(long orgId) { 
		return orgRoleRefRepository.findRolesByOrgId(orgId);
	}

	@Override
	public Page<UumRole> findSelectedRolesByOrgId(long orgId, String roleName, Pageable page) { 
		Page<UumOrgRoleRef> pageTemp = orgRoleRefRepository.findRolesByCon(orgId, roleName, page);
		List<UumRole> roleList = new ArrayList<UumRole>();
		for(UumOrgRoleRef orgRoleRef : pageTemp.getContent()) {
			roleList.add(orgRoleRef.getUumRole());
		}
		
		return new PageImpl<UumRole>(roleList, page, pageTemp.getTotalElements());
	}

	@Override
	public Page<UumRole> findUnselectedRolesByOrgId(long orgId, Pageable page) { 
		List<UumRole> allRoleList = roleService.findAllRoles();
		List<UumRole> selectedRoleList = findSelectedRoleByOrgId(orgId);
		allRoleList.removeAll(selectedRoleList);
		List<UumRole> newRoleList = new ArrayList<UumRole>();
		int count = 0;
		for (UumRole uumRole : allRoleList) {
			count++;
			if (count >= (page.getOffset() + 1) && count <= (page.getPageSize() + page.getOffset())) {
				newRoleList.add(uumRole);
			}
		}
		
		return new PageImpl<UumRole>(newRoleList, page, count);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveOrgRoleRef(long orgId, List<Long> roleIds, HttpServletRequest request) 
			throws ApplicationException {
		
		if(!roleIds.isEmpty()) {
			List<UumOrgRoleRef> orgRoleRefList = new ArrayList<UumOrgRoleRef>();
			StringBuilder optContent = new StringBuilder(); 
			UumOrgan uumOrgan = new UumOrgan();
			uumOrgan.setOrgId(orgId);
			for(Long roleId : roleIds) {
				UumRole uumRole = new UumRole();
				uumRole.setRoleId(roleId);
				
				UumOrgRoleRef uumOrgRoleRef = new UumOrgRoleRef();
				uumOrgRoleRef.setUumOrgan(uumOrgan);
				uumOrgRoleRef.setUumRole(uumRole);
				
				orgRoleRefList.add(uumOrgRoleRef);
				
				optContent.append("机构id:" + orgId + SPLIT + "角色id:" + roleId + SPLIT);
			}
			
			orgRoleRefRepository.save(orgRoleRefList);
			sysOptLogService.saveLog(LOG_LEVEL_FIRST, ROLE_ASSIGN, SAVE_OPT, optContent.toString(), "", request);
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
	private List<UumRole> findSelectedRoleByOrgId(long orgId) {
		List<UumOrgRoleRef> orgRoleRefList = orgRoleRefRepository.findRolesByOrgId(orgId);
		List<UumRole> roleList = new ArrayList<UumRole>();
		if (!orgRoleRefList.isEmpty()) {
			for (UumOrgRoleRef uumOrgRoleRef : orgRoleRefList) {
				roleList.add(uumOrgRoleRef.getUumRole());
			}
		}
		
		return roleList;
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setOrgRoleRefRepository(UumOrgRoleRefRepository orgRoleRefRepository) {
		this.orgRoleRefRepository = orgRoleRefRepository;
	}
	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
	public void setRoleUserService(RoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}
