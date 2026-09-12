package com.mycuckoo.repository.platform;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.domain.platform.DictionaryItem;
import com.mycuckoo.domain.platform.DictionaryItemExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 字典项持久层接口
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 10:00
 */
public interface DictionaryItemMapper extends Repository<DictionaryItem, Long> {

    /**
     * 根据字典ID删除字典项
     *
     * @param dictId 字典ID
     */
    void deleteByDictId(@Param("dictId") long dictId);

    /**
     * 根据字典ID查询字典项
     *
     * @param dictId 字典ID
     * @return 列表
     */
    List<DictionaryItem> findByDictId(@Param("dictId") long dictId);

    /**
     * 根据字典编码查询字典项
     *
     * @param dictCode 字典编码
     * @return 列表
     */
    List<DictionaryItem> findByDictCode(@Param("dictCode") String dictCode);

    /**
     * 根据字典编码查询字典项
     *
     * @param dictCodes 字典编码
     * @return 列表
     */
    List<DictionaryItemExtend> findByDictCodes(@Param("list") List<String> dictCodes);
}