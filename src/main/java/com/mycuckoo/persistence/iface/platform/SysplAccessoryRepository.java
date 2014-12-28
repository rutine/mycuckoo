package com.mycuckoo.persistence.iface.platform;

import java.util.List;

import com.mycuckoo.domain.platform.SysplAccessory;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 附件持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:26:56 AM
 * @version 2.0.0
 */
public interface SysplAccessoryRepository extends Repository<SysplAccessory, Long> {

	/**
	 * 根据附件IDs删除附件
	 * 
	 * @param accessoryIds 附件ID
	 */
	void deleteAccessorysByIds(Long[] accessoryIds);

	/**
	 * 根据公告ID查询附件
	 * 
	 * @param afficheId 公告ID
	 * @return
	 */
	List<SysplAccessory> findAccessorysByAfficheId(Long afficheId);

}
