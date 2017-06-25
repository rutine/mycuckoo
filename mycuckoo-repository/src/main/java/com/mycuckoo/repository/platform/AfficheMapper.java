package com.mycuckoo.repository.platform;

import java.util.Date;
import java.util.List;

import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 公告持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:28:31 AM
 * @version 3.0.0
 */
public interface AfficheMapper extends Repository<Affiche, Long> {

	/**
	 * 查询有效期内的公告
	 * 
	 * @return
	 */
	List<Affiche> findBeforeValidate(Date afficheInvalidate);
}
