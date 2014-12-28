package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 角色持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:46:52 AM
 * @version 2.0.0
 */
public interface UumRoleRepository extends Repository<UumRole, Long> {

	/**
	 * 查询所有角色信息
	 * 
	 * @return
	 */
	List<UumRole> findAllRoles();

	/**
	 * 根据条件分页查询角色
	 * 
	 * @param roleName
	 * @param page
	 * @return
	 */
	Page<UumRole> findRolesByCon(String roleName, Pageable page);

	/**
	 * 根据角色名称的统计角色记录
	 * 
	 * @param roleName
	 * @return
	 */
	int countRolesByRoleName(String roleName);

}
