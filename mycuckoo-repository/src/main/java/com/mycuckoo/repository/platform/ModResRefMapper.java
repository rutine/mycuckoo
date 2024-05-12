package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.ModResRef;
import com.mycuckoo.repository.Repository;

import java.util.List;

/**
 * 功能说明: 模块资源关系持久层接口
 *
 * @author rutine
 * @version 4.1.0
 * @time May 12, 2024 11:33:11 AM
 */
public interface ModResRefMapper extends Repository<ModResRef, Long> {

    /**
     * 根据模块ID删除,级联删除权限 1
     *
     * @param moduleId 模块ID
     */
    void deleteByModuleId(long moduleId);

    /**
     * 根据资源ID删除模, 级联删除权限 1
     *
     * @param resourceId 资源ID
     */
    void deleteByResourceId(long resourceId);

    /**
     * 根据模块ID查询模块操作关系
     *
     * @param moduleId 模块id
     * @return
     */
    List<ModResRef> findByModuleId(Long moduleId);

    /**
     * 根据资源ID查询
     *
     * @param resourceId 资源ID
     * @return
     */
    List<ModResRef> findByResourceId(Long resourceId);

    /**
     * 根据IDs查询
     *
     * @param modOptRefIds
     * @return 列表
     */
    List<ModResRef> findByIds(Long[] modOptRefIds);
}
