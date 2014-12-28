package com.mycuckoo.service.iface.platform;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.vo.SystemConfigBean;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 系统配置文件维护业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:20:56 AM
 * @version 2.0.0
 */
public interface SystemConfigService {

	/**
	 * <p>获取系统配置对象</p>
	 * 
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 8:41:43 PM
	 */
	SystemConfigBean getSystemConfigInfo() throws ApplicationException;

	/**
	 * <p>设置系统配置信息</p>
	 * 
	 * @param systemConfigBean 系统配置对象
	 * @param userAddDelFlag 增加修改标志
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 8:41:55 PM
	 */
	void setSystemConfigInfo(SystemConfigBean systemConfigBean,
			String userAddDelFlag, HttpServletRequest request) throws ApplicationException;
}
