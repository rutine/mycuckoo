package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 机构持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:43:42 AM
 */
public interface OrganMapper extends Repository<Organ, Long> {

    /**
     * 统计所有下级机构数量
     *
     * @param parentId 父级机构ID
     * @return
     */
    int countByParentId(Long parentId);

    /**
     * 根据机构简称统计机构数量
     *
     * @param simpleName 机构简称
     * @return
     */
    int countByOrgSimpleName(String simpleName);

    /**
     * 根据机构ID和过滤ID查询直接下级机构记录
     *
     * @param parentId          父级机构ID
     * @param filterOutOrgId    过滤掉此机构
     * @return
     */
    List<Organ> findByParentIdAndFilterOutOrgId(@Param("parentId") Long parentId, @Param("filterOutOrgId") Long filterOutOrgId);

    /**
     * 根据机构IDs查询机构记录
     * <p>
     * <pre>
     *  数据结构:
     *  [
     *    {orgId, orgSimpleName}, ...
     *  ]
     * </pre>
     *
     * @param orgIds 机构IDs
     * @return
     */
    List<Organ> findByOrgIds(Long[] orgIds);

}
