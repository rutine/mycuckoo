package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.DictBigType;
import com.mycuckoo.repository.Repository;
import org.apache.ibatis.annotations.Param;

/**
 * 功能说明: 字典大类型持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:32:28 AM
 */
public interface DictBigTypeMapper extends Repository<DictBigType, Long> {

    /**
     * 根据字典大类编码统计字典大类数量
     *
     * @param code
     * @return 数量
     */
    int countByCode(String code);

    /**
     * 根据字典大类ID修改字典大类状态
     *
     * @param bigTypeId 典大类ID
     * @param status    新的大类状态
     */
    void updateStatus(@Param("bigTypeId") long bigTypeId, @Param("status") String status);
}
