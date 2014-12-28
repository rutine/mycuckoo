package com.mycuckoo.service.iface.commonused;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 用户常用功能业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:00:09 AM
 * @version 2.0.0
 */
public interface UserCommFunService {
	
	/**
	 * 根据用户ID查询用户常用功能
	 * 
	 * @param userId 用户ID
	 * @return
	 * @author rutine
	 * @time Oct 9, 2012 9:10:22 PM
	 */
	List<UumUserCommfun> findUserCommFunByUserId(long userId);

	/**
	 * 保存用户常用功能
	 * 
	 * @param userId 用户ID
	 * @param moduleIdList 多模块ID
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 9, 2012 9:14:47 PM
	 */
	void saveUserCommFun(long userId, List<Long> moduleIdList,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * 删除用户无用常用功能
	 * 
	 * @param userCommfunList 用户常用功能列表
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 9, 2012 9:15:08 PM
	 */
	void deleteUserCommFunByUserIds(List<UumUserCommfun> userCommfunList,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * 用户停用时删除用户常用功能
	 * 
	 * @param userId
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 9, 2012 9:15:21 PM
	 */
	void deleteUserCommFunByUserId(long userId, HttpServletRequest request)
			throws ApplicationException;
}
