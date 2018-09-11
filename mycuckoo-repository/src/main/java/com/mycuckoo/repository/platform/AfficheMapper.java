package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.repository.Repository;

import java.util.Date;
import java.util.List;

/**
 * 功能说明: 公告持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:28:31 AM
 */
public interface AfficheMapper extends Repository<Affiche, Long> {

    /**
     * 查询有效期内的公告
     *
     * @return
     */
    List<Affiche> findBeforeValidate(Date afficheInvalidate);
}
