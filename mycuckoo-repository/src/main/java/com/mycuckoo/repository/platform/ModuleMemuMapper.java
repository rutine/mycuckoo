package com.mycuckoo.repository.platform;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.platform.ModuleMemu;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 模块持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:37:19 AM
 * @version 3.0.0
 */
public interface ModuleMemuMapper extends Repository<ModuleMemu, Long> {

	/**
	 * 根据父模块<code>ID</code>统计下级模块数
	 * 
	 * @param moduleId 模块ID
	 * @return
	 */
	int countByUpModuleId(long moduleId);

	/**
	 * 判断模块名称是否存在
	 * 
	 * @param moduleName 模块名称
	 * @return
	 */
	int countByModuleName(String moduleName);

	/**
	 * 根据模块ID查询下级模块
	 * 
	 * @param moduleId
	 * @param filterModuleId
	 * @return
	 */
	List<ModuleMemu> findByUpModuleIdAndFilterModuleIds(@Param("moduleId") long moduleId, @Param("filterModuleIds") long[] filterModuleIds);

	/**
	 * 修改行权限
	 * 
	 * @param moduleIds 模块集合
	 * @param rowPrivilegeLevel 权限类型
	 */
	void updateRowPrivilege(Long[] moduleIds, String rowPrivilegeLevel);
}
