package com.mycuckoo.service.iface.uum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 机构业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:27:28 AM
 * @version 2.0.0
 */
public interface OrganService {

	/**
	 * <p>停用/启用机构</p>
	 * 
	 * @param organId 机构ID
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return int 1.机构有下级 2.机构下有角色 3.机构下有用户 4.被其它机构引用 0.停用成功
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 16, 2012 10:50:02 PM
	 */
	int disEnableOrgan(long organId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>判断机构名称是否存在</p>
	 * 
	 * @param organName 机构名称
	 * @return boolean true 为存在 false为不存在
	 * @author rutine
	 * @time Oct 16, 2012 10:54:17 PM
	 */
	boolean existOrganByOrganName(String organName);

	/**
	 * <p>返回所有机构记录</p>
	 * 
	 * @return List<UumOrgan> 所有机构列表
	 * @author rutine
	 * @time Oct 16, 2012 10:50:49 PM
	 */
	List<UumOrgan> findAllOrgans();

	/**
	 * <p>查询所有上/下级机构</p>
	 * 
	 * @param organId 机构ID
	 * @param flag 0为下级，1 为上级
	 * @return List<UumOrgan> 上/下级机构
	 * @author rutine
	 * @time Oct 16, 2012 10:51:09 PM
	 */
	List<UumOrgan> findChildNodes(long organId, int flag);

	/**
	 * <p>查询直接下级机构记录</p>
	 * 
	 * @param organId 机构id
	 * @param filterOrgId 过滤的机构id,修改时过滤掉本机构ID
	 * @return List<TreeVo> 下级机构树节点
	 * @author rutine
	 * @time Oct 16, 2012 10:51:31 PM
	 */
	List<TreeVo> findNextLevelChildNodes(long organId, long filterOrgId);

	/**
	 * <p>查询直接下级机构记录</p>
	 * 
	 * @param organId 机构id
	 * @param filterOrgId 过滤的机构id,修改时过滤掉本机构ID
	 * @return List<TreeVoExtend> 带有复选框的下级机构树节点
	 * @author rutine
	 * @time Oct 16, 2012 10:52:19 PM
	 */
	List<TreeVoExtend> findNextLevelChildNodesWithCheckbox(long organId, long filterOrgId);

	/**
	 * <p>分页查询机构</p>
	 * 
	 * @param treeId 查询某个树的节点
	 * @param orgCode 机构code
	 * @param orgName 机构名称
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 16, 2012 10:53:21 PM
	 */
	Page<UumOrgan> findOrgansByCon(long treeId, String orgCode, String orgName, Pageable page);

	/**
	 * <p>根据机构<code>ID</code>获取机构记录</p>
	 * 
	 * @param orgId 机构ID
	 * @return UumOrgan 机构对象
	 * @author rutine
	 * @time Oct 16, 2012 10:53:59 PM
	 */
	UumOrgan getOrganByOrgId(long orgId);

	/**
	 * <p>根据机构<code>IDs</code>查询机构记录</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[
	 * 		{orgId, orgSimpleName}, ...
	 * 	]
	 * </pre>
	 * 
	 * @param orgIdList
	 * @return
	 * @author rutine
	 * @time Oct 16, 2012 10:55:35 PM
	 */
	List findOrgansByOrgIds(Long[] orgIds);

	/**
	 * <p>更新机构</p>
	 * 
	 * @param uumOrgan 机构对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 16, 2012 10:56:35 PM
	 */
	void updateOrgan(UumOrgan uumOrgan, HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存机构</p>
	 * 
	 * @param uumOrgan 机构对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 16, 2012 10:56:49 PM
	 */
	void saveOrgan(UumOrgan uumOrgan, HttpServletRequest request) throws ApplicationException;
}
