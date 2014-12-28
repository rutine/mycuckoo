package com.mycuckoo.persistence.iface.platform;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysOptLog;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 系统操作日志持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:41:18 AM
 * @version 2.0.0
 */
public interface SysplSysOptLogRepository extends Repository<SysplSysOptLog, Long> {

	/**
	 * 删除日志
	 * 
	 * @param keepdays 保留天数
	 */
	void deleteLogger(int keepdays);

	/**
	 * 分页查找日志
	 * 
	 * @param sysplSysOptLog 查询对象
	 * @param page
	 * @return 分页对象
	 */
	Page<SysplSysOptLog> findOptLogsByCon(SysplSysOptLog sysplSysOptLog,
			Pageable page);

	/**
	 * 根据日志ID获取日志内容
	 * 
	 * @param optId 日志ID
	 * @return
	 * @author rutine
	 */
	String getOptLogContentById(long optId);
}
