package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.core.repository.Repository;

/**
 * 功能说明: 操作按钮持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:39:10 AM
 */
public interface OperateMapper extends Repository<Operate, Long> {

    /**
     * 根据编码统计操作按钮数量
     *
     * @param code 操作按钮编码
     * @return
     */
    int countByCode(String code);

    /**
     * 根据名称统计操作按钮数量
     *
     * @param name 操作按钮名称
     * @return
     */
    int countByName(String name);
}
