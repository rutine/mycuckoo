package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 机构角色关系持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:44:43 AM
 */
public interface OrgRoleRefMapper extends Repository<OrgRoleRef, Long> {

    /**
     * <p>根据orgId查询所有角色.</p>
     * <p>
     * <pre>
     * 	<b>数据结构:</b>
     * 	[
     *        {
     * 			"orgRoleId"    : {@code Long},
     * 			"role"      : {@code List<Role>}
     *        },
     * 		...
     * 	]
     * </pre>
     *
     * @param orgId
     * @return 机构角色关系列表
     **/
    List<OrgRoleRef> findRolesByOrgId(Long orgId);

    /**
     * <p>根据roleId查询所有机构.</p>
     * <p>
     * <pre>
     * 	<b>数据结构:</b>
     * 	[
     *        {
     * 			"orgRoleId"    : {@code Long},
     * 			"organ"     : {@code List<Organ>}
     *        },
     * 		...
     * 	]
     * </pre>
     *
     * @param roleId
     * @return 机构角色关系列表
     **/
    List<OrgRoleRef> findOrgansByRoleId(Long roleId);

    /**
     * <p>根据机构ID和角色ID获取机构角色关系</p>
     *
     * @param orgId  机构ID
     * @param roleId 角色ID
     * @return
     */
    OrgRoleRef getByOrgRoleId(@Param("orgId") Long orgId, @Param("roleId") Long roleId);

    /**
     * <p>根据机构ID和角色名称分页查询已分配的角色</p>
     *
     * @param orgId    机构ID
     * @param roleName 角色名称 like: '%keyword%'
     * @param page
     * @return
     */
    Page<OrgRoleRef> findRolesByPage(@Param("orgId") Long orgId, @Param("roleName") String roleName, Pageable page);

    /**
     * <p>根据角色ID查询所有机构角色关系ID</p>
     *
     * @param roleId
     * @return
     */
    List<Long> findOrgRoleIdsByRoleId(Long roleId);

    /**
     * <p>根据机构ID统计角色数量</p>
     *
     * @param orgId
     * @return
     */
    int countByOrgId(Long orgId);

    /**
     * <p>根据角色ID删除机构角色关系记录，为停用角色所用</p>
     *
     * @param roleId
     */
    void deleteByRoleId(Long roleId);

    /**
     * <p>根据机构角色IDs查询机构角色关系</p>
     *
     * @param orgRoleRefIds
     * @return
     */
    List<OrgRoleRef> findByOrgRoleIds(Long[] orgRoleRefIds);
}
