package com.mycuckoo.service.iface.platform;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysOptLog;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 系统操作日志业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:20:04 AM
 * @version 2.0.0
 */
public interface SystemOptLogService {

	/**
	 * <p>删除日志</p>
	 * 
	 * @param keepdays 保留天数
	 * @author rutine
	 * @time Oct 7, 2012 3:28:25 PM
	 */
	void deleteLog(int keepdays);

	/**
	 * <p>分页查询日志</p>
	 * 
	 * @param sysplSysOptLog 查询对象
	 * @param page 分页对象
	 * @return
	 * @author rutine
	 * @time Oct 7, 2012 3:31:03 PM
	 */
	Page<SysplSysOptLog> findOptLogsByCon(SysplSysOptLog sysplSysOptLog, Pageable page);

	/**
	 * <p>根据<code>ID</code>获取操作日志内容
	 * 
	 * @param optId 操作ID
	 * @return
	 * @author rutine
	 * @time Oct 7, 2012 3:32:33 PM
	 */
	String getOptLogContentById(long optId);

	/**
	 * <p>保存日志</p>
	 * 
	 * @param level 日志级别
	 * @param optModName 操作模块名称
	 * @param optName 操作名称
	 * @param optContent 操作内容
	 * @param optBusinessId
	 * @param request
	 * @author rutine
	 * @throws ApplicationException
	 * @time Oct 7, 2012 5:30:21 PM
	 */
	void saveLog(String level, String optModName, String optName,
			String optContent, String optBusinessId, HttpServletRequest request)
			throws ApplicationException;
}
