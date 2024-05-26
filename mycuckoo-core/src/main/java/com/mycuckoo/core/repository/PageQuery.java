package com.mycuckoo.core.repository;

import com.mycuckoo.core.repository.param.Column;

import java.util.List;
import java.util.Map;

/**
 * 功能说明: 查询参数
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:14
 */
public interface PageQuery extends Pageable {
    //页码
    int getPageNo();

    //每页大小
    int getPageSize();

    //查询关键词
    String getKeyword();

    //公共查询参数
    Map<String, Object> getQ() ;

    //查找去重字段
    String getDistinct();

    //列属性列表
    List<? extends Column> getColumns();

    //查询参数
    Map<String, Object> getWhere();

    //排序参数
    Map<String, String> getSort();

    //聚合参数
    Map<String, String> getAggr();
}
