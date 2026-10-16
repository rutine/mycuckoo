package com.mycuckoo.admin.repository.uum;

import com.mycuckoo.core.annotation.RowPermission;
import com.mycuckoo.admin.entity.uum.Department;
import com.mycuckoo.admin.entity.uum.DepartmentExtend;
import com.mycuckoo.core.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 部门持久层接口
 *
 * @author rutine
 * @version 4.1.0
 * @time May 19, 2024 11:40:13 AM
 */
@RowPermission(table = "uum_department")
public interface DepartmentMapper extends Repository<Department, Long> {

    int updateTreeId(@Param("oldParentTreeId") String oldParentTreeId,
                     @Param("newParentTreeId") String newParentTreeId,
                     @Param("level") Integer level);

    /**
     * 统计所有下级数量
     *
     * @param parentId 父级ID
     */
    int countByParentId(Long parentId);

    /**
     * 根据IDs查询记录
     * <pre>
     *  数据结构:
     *  [{deptId, name}, ... ]
     * </pre>
     *
     * @param deptIds
     */
    List<DepartmentExtend> findByDeptIds(Long[] deptIds);
}
