package com.mycuckoo.service.iface.platform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplAffiche;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 公告业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:10:25 AM
 * @version 2.0.0
 */
public interface AfficheService {

	/**
	 * <p>删除公告记录</p>
	 * 
	 * @param afficheIdList 公告<code>ID</code>列表
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 26, 2012 8:40:54 PM
	 */
	void deleteAffichesByIds(List<Long> afficheIdList, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>根据公告<code>ID</code>获取公告记录</p>
	 * 
	 * @param afficheId 公告<code>ID</code>
	 * @return
	 * @author rutine
	 * @time Oct 26, 2012 8:41:30 PM
	 */
	SysplAffiche getAfficheByAfficheId(Long afficheId);

	/**
	 * <p>分页查询公告</p>
	 * 
	 * @param afficheTitle 公告标题
	 * @param affichePulish 公告是否发布
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 26, 2012 8:42:15 PM
	 */
	Page<SysplAffiche> findAffichesByCon(String afficheTitle, Short affichePulish, Pageable page);

	/**
	 * <p>查询所有有效期内的公告记录</p>
	 * 
	 * @author rutine
	 * @return List<SysplAffiche> 所有公告信息
	 * @time Dec 1, 2010 2:51:21 PM
	 */
	List<SysplAffiche> findAffichesBeforeValidate();

	/**
	 * <p>修改公告</p>
	 * 
	 * @param affiche 公告对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 26, 2012 8:43:08 PM
	 */
	void updateAffiche(SysplAffiche affiche, HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存公告</p>
	 * 
	 * @param affiche 公告对象
	 * @param accessoryNameList 附件名列表
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 26, 2012 8:43:29 PM
	 */
	void saveAffiche(SysplAffiche affiche, List<String> accessoryNameList, HttpServletRequest request)
			throws ApplicationException;

}
