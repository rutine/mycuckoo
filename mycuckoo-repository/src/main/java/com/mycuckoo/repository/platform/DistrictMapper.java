package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.District;
import com.mycuckoo.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 地区持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:35:12 AM
 */
public interface DistrictMapper extends Repository<District, Long> {

    /**
     * 根据地区ID统计下级地区数
     *
     * @param parentId 父地区ID
     * @return 下级数量
     */
    int countByParentId(long parentId);

    /**
     * 根据地区名称判断地区是否存在
     *
     * @param districtName 地区名称
     * @return 地区数量
     */
    int countByDistrictName(String districtName);

    /**
     * 根据地区ID查询下级地区
     *
     * @param parentId 父级ID
     * @return 下级地区
     */
    List<District> findByParentId(Long parentId);

    /**
     * 根据地区ID和过滤条件查询下级地区
     *
     * @param parentId     父地区ID
     * @param filterOutDistrictIds 过滤id,当修改时将本ID过滤掉
     * @return
     */
    List<District> findByParentIdAndFilterOutIds(@Param("parentId") long parentId,
                                              @Param("filterOutDistrictIds") long[] filterOutDistrictIds);
}
