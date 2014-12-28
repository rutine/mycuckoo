package com.mycuckoo.service.iface.commonused;

import java.util.List;
import java.util.Map;

import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.service.iface.login.LoginService;
import com.mycuckoo.service.iface.platform.ModuleService;

/**
 * 功能说明: 系统平台对外的公共接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:06:03 AM
 * @version 2.0.0
 */
public interface PlatformCommonService {

	/**
	 * <p>过滤模块一二三级 
	 * 	源自：{@link ModuleService#findAllModules()}
	 * 	应用：{@link LoginService#findUserPrivilegesForAdminLogin()}</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	查看提供者 {@link ModuleService#filterModule(List)}
	 * </pre>
	 * 
	 * @param list 需要过滤的模块列表
	 * @return Map 1:list 2:map 3:map
	 * @author rutine
	 * @time Oct 16, 2012 10:33:43 PM
	 */
	Map filterModuleMenu(List<SysplModuleMemu> list);

	/**
	 * <p>查询所有模块操作关系记录</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	查看提供者 {@link ModuleService#findAllModOptRefs()}
	 * </pre>
	 * 
	 * @return List<SysplModOptRef> 所有模块操作关系记录
	 * @author rutine
	 * @time Oct 16, 2012 10:34:27 PM
	 */
	List<SysplModOptRef> findAllModOptRefs();

	/**
	 * <p>查询所有模块信息 
	 * 	源自：{@link ModuleService#findAllModules()}
	 * 	应用：{@link LoginService#findUserPrivilegesForAdminLogin()}</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	查看提供者 {@link ModuleService#findAllModules()}
	 * </pre>
	 * 
	 * @return List<SysplModuleMemu> 所有模块信息
	 * @author rutine
	 * @time Oct 16, 2012 10:32:32 PM
	 */
	List<SysplModuleMemu> findAllModules();

	/**
	 * 根据地区ID获取地区
	 * 
	 * @param districtId 地区ID
	 * @return sysplDistrict 地区对象
	 * @author rutine
	 * @time Oct 16, 2012 10:35:24 PM
	 */
	SysplDistrict getDistrictByDistrictId(Long districtId);

	/**
	 * <p>根据模块关系表ID查询所有模块操作关系</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	查看提供者: {@link ModuleService#findModOptRefsByModOptRefIds(Long[])}
	 * </pre>
	 * 
	 * @param modOptRefIdList
	 * @return
	 * @author rutine
	 * @time Oct 16, 2012 10:36:35 PM
	 */
	List<SysplModOptRef> findModOptRefByModOptRefIds(List<Long> modOptRefIdList);

	/**
	 * 根据参数名称查找系统参数
	 * 
	 * @param paraName
	 * @return
	 * @author rutine
	 * @time Oct 16, 2012 10:35:07 PM
	 */
	String findSystemParaByParaName(String paraName);
}
