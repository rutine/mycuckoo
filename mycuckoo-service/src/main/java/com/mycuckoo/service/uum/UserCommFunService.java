package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.USER_COMM_FUN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.UserCommfun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.uum.UserCommFunMapper;
import com.mycuckoo.service.platform.SystemOptLogService;

/**
 * 功能说明: 用户常用功能业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:11:53 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class UserCommFunService {
	
	
	@Autowired
	private UserCommFunMapper userCommFunMapper;
	@Autowired
	private SystemOptLogService sysOptLogService;

	
	public List<UserCommfun> findByUserId(long userId) {
		return userCommFunMapper.findByUserId(userId);
	}

	public void save(long userId, List<Long> moduleIdList) throws ApplicationException {
		userCommFunMapper.deleteByUserId(userId);
		
		StringBuilder optContent = new StringBuilder();
		optContent.append("用户常用功能保存:用户ID").append(userId).append(SPLIT).append("模块ID：");
		if(moduleIdList != null) {
			for(Long moduleId : moduleIdList) {
				UserCommfun userCommfunId = new UserCommfun(userId, moduleId);
				userCommFunMapper.save(userCommfunId);
				
				optContent.append(moduleId).append(",");
			}
			
		}
		
		sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, USER_COMM_FUN, optContent.toString(), "");
	}

	public void deleteByUserIds(List<UserCommfun> userCommfunList) throws ApplicationException {
		for(UserCommfun userCommfun : userCommfunList) {
			userCommFunMapper.delete(userCommfun);
		}
	}

	public void deleteByUserId(long userId) throws ApplicationException {
		userCommFunMapper.deleteByUserId(userId);
	}

}
