package com.mycuckoo.repository.platform;

import java.util.List;

import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 附件持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:26:56 AM
 * @version 3.0.0
 */
public interface AccessoryMapper extends Repository<Accessory, Long> {

	/**
	 * 根据公告ID查询附件
	 * 
	 * @param afficheId 公告ID
	 * @return
	 */
	List<Accessory> findByAfficheId(Long afficheId);

}
