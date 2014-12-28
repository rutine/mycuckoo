package com.mycuckoo.service.iface.platform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.platform.SysplAccessory;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 附件业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:09:35 AM
 * @version 2.0.0
 */
public interface AccessoryService {

	/**
	 * <p>根据IDs删除附件</p>
	 * 
	 * @param accessoryIds 附件IDs
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 25, 2012 10:44:54 PM
	 */
	void deleteAccessorysByIds(List<Long> accessoryIds,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据公告ID查询附件</p>
	 * 
	 * @param id 公告ID
	 * @return
	 * @author rutine
	 * @time Oct 25, 2012 10:46:11 PM
	 */
	List<SysplAccessory> findAccessorysByAfficheId(long afficheId);

	/**
	 * <p>根据附件ID获取附件</p>
	 * 
	 * @param accessoryId 附件ID
	 * @return
	 * @author rutine
	 * @time Oct 25, 2012 10:46:50 PM
	 */
	SysplAccessory getAccessoryByAccId(long accessoryId);

	/**
	 * <p>保存附件</p>
	 * 
	 * @param accessory 附件对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 25, 2012 10:47:14 PM
	 */
	void saveAccessory(SysplAccessory accessory, HttpServletRequest request)
			throws ApplicationException;
}
