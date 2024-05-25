package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.core.repository.Repository;

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
     * @param key 参数键
     * @return 数量
     */
    int countByKey(String key);

    /**
     * 根据参数键查找系统参数
     *
     * @param key
     * @return 系统参数对象
     */
    SysParameter getByKey(String key);
}
