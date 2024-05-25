package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.core.repository.Repository;

/**
 * 功能说明: 编码持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:29:48 AM
 */
public interface CodeMapper extends Repository<Code, Long> {

    /**
     * 根据编码统计编码是否存在
     *
     * @param code 编码
     * @return
     */
    int countByCode(String code);

    /**
     * 根据编码获取编码
     *
     * @param code 编码
     * @return
     */
    Code getByCode(String code);
}
