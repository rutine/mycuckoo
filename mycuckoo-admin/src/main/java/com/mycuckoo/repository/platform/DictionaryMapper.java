package com.mycuckoo.repository.platform;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.domain.platform.Dictionary;
import org.apache.ibatis.annotations.Param;

/**
 * 功能说明: 字典持久层接口
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 10:00
 */
public interface DictionaryMapper extends Repository<Dictionary, Long> {

    /**
     * 根据字典编码统计字典数量
     *
     * @param code 字典编码
     * @return 数量
     */
    int countByCode(String code);

    /**
     * 根据字典ID修改字典状态
     *
     * @param dictId 字典ID
     * @param status 新的字典状态
     */
    void updateStatus(@Param("dictId") long dictId, @Param("status") String status);
}