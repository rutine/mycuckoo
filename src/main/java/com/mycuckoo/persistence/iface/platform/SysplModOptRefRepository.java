package com.mycuckoo.persistence.iface.platform;

import java.util.List;

import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 模块操作关系持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:36:26 AM
 * @version 2.0.0
 */
public interface SysplModOptRefRepository extends
		Repository<SysplModOptRef, Long> {

	/**
	 * 根据模块ID删除模块操作关系,级联删除权限 1
	 * 
	 * @param moduleId 模块ID
	 */
	void deleteModOptRefByModuleId(long moduleId);

	/**
	 * 根据操作ID删除模块操作关系, 级联删除权限 1
	 * 
	 * @param operateId 操作ID
	 */
	void deleteModOptRefByOperateId(long operateId);

	/**
	 * 根据模块ID查询模块操作关系
	 * 
	 * @param moduleId 模块id
	 * @return 所有关联的模块操作关系
	 */
	List<SysplModOptRef> findModOptRefsByModuleId(Long moduleId);

	/**
	 * 根据操作ID查询模块操作关系
	 * 
	 * @param operateId 操作id
	 * @return 所有关联的模块操作关系
	 */
	List<SysplModOptRef> findModOptRefsByOperateId(Long operateId);

	/**
	 * 根据模块操作关系IDs查询模块操作关系
	 * 
	 * @param modOptRefIds
	 * @return 列表
	 */
	List<SysplModOptRef> findModOptRefsByIds(Long[] modOptRefIds);
}
