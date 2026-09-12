package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.District;
import com.mycuckoo.core.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 地区持久层接口
 *
 * @author rutine
 * @version 5.0.0
 * @time Sep 24, 2014 10:35:12 AM
 */
public interface DistrictMapper extends Repository<District, Long> {

    /**
     * 根据地区父code统计下级地区数
     *
     * @param parentCode 父code
     * @return 下级数量
     */
    int countByParentCode(String parentCode);

    /**
     * 根据地区名称判断地区是否存在
     *
     * @param districtName 地区名称
     * @return 地区数量
     */
    int countByName(String districtName);

    /**
     * 根据地区code查询地区
     *
     * @param code
     * @return 地区
     */
    District getByCode(String code);

    /**
     * 根据地区ID和过滤条件查询下级地区
     *
     * @param parentCode    父级code
     * @param ignoreIds     过滤id,当修改时将本ID过滤掉
     * @return
     */
    List<District> findByParentCodeAndIgnoreIds(@Param("parentCode") String parentCode,
                                                @Param("ignoreIds") long[] ignoreIds);
}
