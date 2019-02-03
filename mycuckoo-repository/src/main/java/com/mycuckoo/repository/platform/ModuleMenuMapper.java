package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 模块持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:37:19 AM
 */
public interface ModuleMenuMapper extends Repository<ModuleMenu, Long> {

    /**
     * 根据父模块<code>ID</code>统计下级模块数
     *
     * @param parentId 模块ID
     * @return
     */
    int countByParentId(long parentId);

    /**
     * 判断模块英文名称是否存在
     *
     * @param modEnName 模块英文名称
     * @return
     */
    int countByModEnName(String modEnName);

    /**
     * 根据模块ID查询下级模块
     *
     * @param parentId
     * @param ignoreModuleIds
     * @return
     */
    List<ModuleMenu> findByParentIdAndIgnoreModuleIds(
            @Param("parentId") long parentId,
            @Param("ignoreModuleIds") long[] ignoreModuleIds);
}
