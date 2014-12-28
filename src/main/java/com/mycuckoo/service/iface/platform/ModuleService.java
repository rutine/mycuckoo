package com.mycuckoo.service.iface.platform;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 系统模块业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:17:09 AM
 * @version 2.0.0
 */
public interface ModuleService {

	/**
	 * <p>根据操作<code>ID</code>删除模块操作关系, 级联删除权限</p>
	 * 
	 * @param operateId 操作ID
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 8:59:28 PM
	 */
	void deleteModOptRefByOperateId(long operateId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>删除模块</p>
	 * 
	 * @param moduleId 模块ID
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 8:58:52 PM
	 */
	void deleteModuleByModuleId(Long moduleId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>停用/启用模块</p>
	 * 
	 * @param moduleId 模块ID
	 * @param disableFlag 停用/启用标志
	 * @param request
	 * @return boolean true为停用/启用成功, false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 8:57:55 PM
	 */
	boolean disEnableModule(long moduleId, String disableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据模块{@code id}和上级模块{@code id}过滤模块, 形成1,2,3三级的模块{@code Map}对象</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		{@code "first"}  : {@code List<SysplModuelMenu>},
	 * 		{@code "second"} : {@code Map<String, List<SysplModuleMemu>>}(key键值是上级模块id),
	 * 		{@code "third"}  : {@code Map<String, List<SysplModuleMemu>>}(key键值是上级模块id)
	 * 	}
	 * </pre>
	 * 
	 * @param list 需要过滤的模块列表
	 * @return Map {"first" : List, "second" : Map, "third" : Map}
	 * @author rutine
	 * @time Oct 10, 2012 9:14:25 PM
	 */
	Map<String, Object> filterModule(List<SysplModuleMemu> list);

	/**
	 * <p>返回所有模块信息</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		moduleId, sysplModuleMemu.moduleId, upModId, modName, modEnId, 
	 * 		modImgCls, modLevel, modOrder, belongToSys, modPageType, 
	 * 		memo, status, creator, createDate
	 * 	}
	 * </pre>
	 * 
	 * @return List<SysplModuleMemu> 所有模块信息
	 * @author rutine
	 * @time Oct 10, 2012 9:08:28 PM
	 */
	List<SysplModuleMemu> findAllModules();

	/**
	 * <p>返回所有模块操作关系记录</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[{
	 * 		{@code "modOptId"} : {@link Long}(id值),
	 * 		{@code "sysplOperate"} : {@link SysplOperate}(操作按钮对象,所有表对应字段),
	 * 		{@code "sysplModuleMemu"} : {@link SysplModuleMemu}(模块对象, 所有表对应字段(3级模块), 可级联到最上级模块(1级模块))
	 * 	}]
	 * </pre>
	 * 
	 * @return List<SysplModOptRef> 所有模块操作关系记录
	 * @author rutine
	 * @time Oct 10, 2012 9:22:33 PM
	 */
	List<SysplModOptRef> findAllModOptRefs();

	/**
	 * <p>查询模块已经分配和未分配的操作列表</p>
	 * <pre>
	 * 	<b>数据结构:</b>
	 * {
	 * 		{@code "assignedModOpts"} : {@code List<SysplOperate>},
	 * 		{@code "unassignedModOpts"} : {@code List<SysplOperate>}
	 * 	}
	 * </pre>
	 * 
	 * @param moduleId 模块ID
	 * @return Map<String,List<SysplOperate>> 1 list 分配 2 list 未分配
	 * @author rutine
	 * @time Oct 10, 2012 9:20:51 PM
	 */
	Map<String, List<SysplOperate>> findAssignedAUnAssignedOperatesByModuleId(long moduleId);

	/**
	 * 根据模块ID查询已经分配的模块操作
	 * 
	 * @param moduleId 模块ID
	 * @return List<SysplOperate> 已经分配的模块操作list
	 * @author rutine
	 * @time Oct 10, 2012 9:18:49 PM
	 */
	List<SysplOperate> findAssignedOperatesByModuleId(long moduleId);

	/**
	 * <p>查找模块<code>ID</code>的下级模块</p>
	 * 
	 * @param moduleId 模块<code>id</code>
	 * @param filterModuleId 过滤模块id,当修改模块菜单时将本模块<code>id</code>过滤掉
	 * @return List<TreeVo> 下级模块菜单
	 * @author rutine
	 * @time Oct 10, 2012 9:17:41 PM
	 */
	List<TreeVo> findModulesByUpModId(long moduleId, long filterModuleId);

	/**
	 * 根据模块id查询模块所有子节点
	 * 
	 * @param moduleId 上级模块id
	 * @param flag 0为下级，1 为上级
	 * @return
	 * @author rutine
	 * @time Oct 10, 2012 9:12:16 PM
	 */
	List<SysplModuleMemu> findChildNodeList(long moduleId, int flag);

	/**
	 * <p>根据模块关系表ID查询模块操作关系</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[{
	 * 		{@code modOptId} : {@link Long}(id值),
	 * 		{@code sysplOperate} : {@link SysplOperate}(操作按钮对象,所有表对应字段),
	 * 		{@code sysplModuleMemu} : {@link SysplModuleMemu}(模块对象, 所有表对应字段(3级模块), 可级联到最上级模块(1级模块))
	 * 	}]
	 * </pre>
	 * 
	 * @param modOptRefIds
	 * @return
	 * @author rutine
	 * @time Oct 10, 2012 9:28:26 PM
	 */
	List<SysplModOptRef> findModOptRefsByModOptRefIds(Long[] modOptRefIds);

	/**
	 * <p>分页查询模块</p>
	 * 
	 * @param treeId 查询某个树的节点
	 * @param modName 模块名称
	 * @param modEnId 模块英文ID
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 10, 2012 9:06:30 PM
	 */
	Page<SysplModuleMemu> findModulesByCon(long treeId, String modName, String modEnId, Pageable page);

	/**
	 * <p>获取模块</p>
	 * 
	 * @param moduleId
	 * @return
	 * @author rutine
	 * @time Oct 10, 2012 9:00:17 PM
	 */
	SysplModuleMemu getModuleByModuleId(Long moduleId);

	/**
	 * <p>判断模块名称是否存在</p>
	 * 
	 * @param moduleName 模块名称
	 * @return boolean true 模块名存在 false 不存在
	 * @author rutine
	 * @time Oct 10, 2012 9:05:14 PM
	 */
	boolean existsModuleByModuleName(String moduleName);

	/**
	 * <p>修改模块</p>
	 * 
	 * @param moduleMemu 模块对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 10:44:03 PM
	 */
	void updateModule(SysplModuleMemu moduleMemu, HttpServletRequest request) throws ApplicationException;

	/**
	 * 修改模块标签描述
	 * 
	 * @param xmlFile 文件名路径
	 * @param moduleLabelList [0]为xml path name [1]为标签描述
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 9:23:06 PM
	 * @deprecated
	 */
	void updateModuleLabel(String xmlFile, List<String[]> moduleLabelList,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存模块</p>
	 * 
	 * @param moduleMemu 系统模块
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 8:56:29 PM
	 */
	void saveModule(SysplModuleMemu moduleMemu, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>保存'模块-操作'关系</p>
	 * 
	 * @param moduleId 模块<code>ID</code>
	 * @param operateIdList 操作<code>ID</code>列表
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 9:21:53 PM
	 */
	void saveModuleOptRefs(long moduleId, List<Long> operateIdList,
			HttpServletRequest request) throws ApplicationException;
}
