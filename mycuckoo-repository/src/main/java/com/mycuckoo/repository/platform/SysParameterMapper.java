package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 系统参数持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:42:09 AM
 */
public interface SysParameterMapper extends Repository<SysParameter, Long> {

    /**
     * 判断参数键是否存在
     *
     * @param paraKeyName 参数键
     * @return 数量
     */
    int countByParaKeyName(String paraKeyName);

    /**
     * 根据参数名称获取系统参数
     *
     * @param paraName
     * @return 系统参数对象
     */
    SysParameter getByParaName(String paraName);
}
