package com.mycuckoo.repository.platform;

import java.util.Date;

import com.mycuckoo.domain.platform.SysOptLog;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 系统操作日志持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:41:18 AM
 * @version 3.0.0
 */
public interface SysOptLogMapper extends Repository<SysOptLog, Long> {

	/**
	 * 删除日志
	 * 
	 * @param optTime 保留天数
	 */
	void deleteLogger(Date optTime);

	/**
	 * 根据日志ID获取日志内容
	 * 
	 * @param optId 日志ID
	 * @return
	 * @author rutine
	 */
	String getOptContentById(long optId);
}
