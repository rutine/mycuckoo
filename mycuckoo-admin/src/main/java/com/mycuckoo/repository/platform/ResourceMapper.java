package com.mycuckoo.repository.platform;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.domain.platform.Resource;

/**
 * 功能说明: 资源持久层接口
 *
 * @author rutine
 * @version 4.1.0
 * @time May 5, 2024 10:12:15 AM
 */
public interface ResourceMapper extends Repository<Resource, Long> {
    /**
     * 判断是否存在所给id的实体对象
     *
     * @param id 不能为null
     * @return true 如果id对应的实体, 否则为false
     * @throws
     */
    boolean exists(Long id);
}
