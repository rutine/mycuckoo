package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.repository.Repository;

import java.util.List;

/**
 * 功能说明: 模块操作关系持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:36:26 AM
 */
public interface ModOptRefMapper extends Repository<ModOptRef, Long> {

    /**
     * 根据模块ID删除模块操作关系,级联删除权限 1
     *
     * @param moduleId 模块ID
     */
    void deleteByModuleId(long moduleId);

    /**
     * 根据操作ID删除模块操作关系, 级联删除权限 1
     *
     * @param operateId 操作ID
     */
    void deleteByOperateId(long operateId);

    /**
     * 根据模块ID查询模块操作关系
     *
     * @param moduleId 模块id
     * @return 所有关联的模块操作关系
     */
    List<ModOptRef> findByModuleId(Long moduleId);

    /**
     * 根据操作ID查询模块操作关系
     *
     * @param operateId 操作id
     * @return 所有关联的模块操作关系
     */
    List<ModOptRef> findByOperateId(Long operateId);
}
