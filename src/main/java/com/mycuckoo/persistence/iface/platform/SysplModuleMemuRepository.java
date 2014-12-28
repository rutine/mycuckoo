package com.mycuckoo.persistence.iface.platform;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 模块持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:37:19 AM
 * @version 2.0.0
 */
public interface SysplModuleMemuRepository extends Repository<SysplModuleMemu, Long> {

	/**
	 * 根据父模块<code>ID</code>统计下级模块数
	 * 
	 * @param moduleId 模块ID
	 * @return
	 */
	int countModulesByUpModId(long moduleId);

	/**
	 * 判断模块名称是否存在
	 * 
	 * @param moduleName 模块名称
	 * @return
	 */
	int countModulesByModuleName(String moduleName);

	/**
	 * 根据模块ID删除模块操作关系,级联删除权限 1
	 * 
	 * @param moduleId 模块ID
	 */
	void deleteModOptRefByModuleId(long moduleId);

	/**
	 * 根据操作ID删除模块操作关系,级联删除权限 2
	 * 
	 * @param operateId 操作id
	 */
	void deleteModOptRefByOperateId(long operateId);

	/**
	 * 
	 * 保存模块时删除无用的模块操作关系
	 * 
	 * @param modOptRefs 模块操作关系集合
	 * @author rutine
	 */
	void deleteModOptRefs(List<SysplModOptRef> modOptRefs);

	/**
	 * 返回所有模块记录
	 * 
	 * @return
	 */
	List<SysplModuleMemu> findAllModules();

	/**
	 * 返回所有模块操作关系记录
	 * 
	 * @return
	 */
	List<SysplModOptRef> findAllModOptRefs();

	/**
	 * 根据模块ID查询模块操作关系
	 * 
	 * @param moduleId 模块ID
	 * @return
	 */
	List<SysplModOptRef> findModOptRefsByModuleId(long moduleId);

	/**
	 * 根据模块ID查询已经分配的模块操作关系
	 * 
	 * @param moduleId 模块ID
	 * @return List<SysplModOptRef>
	 */
	List<SysplModOptRef> findAssignedModOptRefsByModuleId(long moduleId);
	
	/**
	 * 根据操作ID查询模块操作关系
	 * 
	 * @param operateId 操作id
	 * @return 所有关联的模块操作关系
	 */
	List<SysplModOptRef> findModOptRefsByOperateId(Long operateId);

	/**
	 * 根据模块ID查询下级模块
	 * 
	 * @param moduleId
	 * @param filterModuleId
	 * @return
	 */
	List<SysplModuleMemu> findModulesByUpModId(long moduleId, long filterModuleId);

	/**
	 * 根据条件分页查询模块
	 * 
	 * @param ids 模块ids
	 * @param modName 模块名称
	 * @param modEnId 模块英文名
	 * @param page
	 * @return
	 */
	Page<SysplModuleMemu> findModulesByCon(Long[] ids, String modName,
			String modEnId, Pageable page);

	/**
	 * 根据模块操作关系ID查询模块操作关系
	 * 
	 * @param modOptRefIds
	 * @return
	 */
	List<SysplModOptRef> findModOptRefsByModOptRefIds(Long[] modOptRefIds);

	/**
	 * 修改行权限
	 * 
	 * @param moduleIds 模块集合
	 * @param rowPrivilegeLevel 权限类型
	 */
	void updateModuleRowPrivilege(Long[] moduleIds, String rowPrivilegeLevel);

	/**
	 * 保存模块时级联保存模块操作关系
	 * 
	 * @param modOptRefs 模块操作关系集合
	 * @author rutine
	 */
	void saveModOptRef(List<SysplModOptRef> modOptRefs);
}
