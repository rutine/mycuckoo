package com.mycuckoo.service.impl.commonused;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.USER_COMM_FUN;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumUserCommFunRepository;
import com.mycuckoo.service.iface.commonused.UserCommFunService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 用户常用功能业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:11:53 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserCommFunServiceImpl implements UserCommFunService {
	
	private SystemOptLogService sysOptLogService;
	private UumUserCommFunRepository userCommFunRepository;

	@Override
	public List<UumUserCommfun> findUserCommFunByUserId(long userId) {
		return userCommFunRepository.findUserCommFunByUserId(userId);
	}

	@Override
	public void saveUserCommFun(long userId, List<Long> moduleIdList,  HttpServletRequest request) 
			throws ApplicationException {
		
		userCommFunRepository.deleteUserCommFun(userId);
		String moduleIds = "";
		if(moduleIdList != null && !moduleIdList.isEmpty()) {
			List<UumUserCommfun> userCommfunList = new ArrayList<UumUserCommfun>();
			for(Long moduleId : moduleIdList) {
				UumUserCommfun userCommfunId = new UumUserCommfun(userId, moduleId);
				
				moduleIds = "".equals(moduleId) ? moduleId + "" : moduleIds + "," + moduleId;
				userCommfunList.add(userCommfunId);
			}
			userCommFunRepository.save(userCommfunList);
		}
		
		String optContent = "用户常用功能保存:用户ID" + userId + SPLIT + "模块ID："+moduleIds;
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, USER_COMM_FUN, SAVE_OPT, optContent.toString(), "", request);
	}

	@Override
	public void deleteUserCommFunByUserIds(List<UumUserCommfun> userCommfunList, HttpServletRequest request) 
			throws ApplicationException {
		
		for(UumUserCommfun userCommfun : userCommfunList) {
			userCommFunRepository.delete(userCommfun);
		}
	}

	@Override
	public void deleteUserCommFunByUserId(long userId, HttpServletRequest request) 
			throws ApplicationException {
		
		userCommFunRepository.deleteUserCommFun(userId);
	}
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	@Autowired
	public void setUserCommFunRepository(UumUserCommFunRepository userCommFunRepository) {
		this.userCommFunRepository = userCommFunRepository;
	}

}
