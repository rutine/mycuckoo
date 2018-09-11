package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.repository.Repository;

import java.util.List;

/**
 * 功能说明: 字典小类型持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:33:43 AM
 */
public interface DicSmallTypeMapper extends Repository<DicSmallType, Long> {

    /**
     * 根据大类ID删除小类
     *
     * @param bigTypeId 大类代码
     */
    void deleteByBigTypeId(long bigTypeId);

    /**
     * 根据大类ID查询小类
     *
     * @param bigTypeId
     * @return 列表
     */
    List<DicSmallType> findByBigTypeId(long bigTypeId);

    /**
     * 根据大类代码查询小类
     *
     * @param bigTypeCode 大类代码
     * @return 列表
     */
    List<DicSmallType> findByBigTypeCode(String bigTypeCode);

}
