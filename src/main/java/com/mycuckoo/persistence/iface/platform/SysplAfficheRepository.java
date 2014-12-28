package com.mycuckoo.persistence.iface.platform;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplAffiche;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 公告持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:28:31 AM
 * @version 2.0.0
 */
public interface SysplAfficheRepository extends Repository<SysplAffiche, Long> {

	/**
	 * 删除公告
	 * 
	 * @param afficheIds 公告id
	 */
	void deleteAffichesByIds(Long[] afficheIds);

	/**
	 * 根据条件分页查询公告
	 * 
	 * @param afficheTitle 公告标题
	 * @param affichePulish 公告是否发布
	 * @param page
	 * @return 分页对象
	 */
	Page<SysplAffiche> findAffichesByCon(String afficheTitle, Short affichePulish, Pageable page);

	/**
	 * 查询有效期内的公告
	 * 
	 * @return
	 */
	List<SysplAffiche> findAffichesBeforeValidate();
}
