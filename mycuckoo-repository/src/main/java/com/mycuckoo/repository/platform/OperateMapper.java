package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 操作按钮持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:39:10 AM
 */
public interface OperateMapper extends Repository<Operate, Long> {

    /**
     * 根据名称统计操作按钮数量
     *
     * @param operateName 操作按钮名称
     * @return
     */
    int countByName(String operateName);
}
