package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_MODOPT_MGR;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplOperateRepository;
import com.mycuckoo.service.iface.platform.OperateService;
import com.mycuckoo.service.iface.platform.ModuleService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 模块操作业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:35:54 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class OperateServiceImpl implements OperateService {
	
	static Logger logger = LoggerFactory.getLogger(OperateServiceImpl.class);
	private SysplOperateRepository operateRepository;
	private ModuleService moduleService;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableOperate(long operateId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			SysplOperate sysplOperate = findOperatesByOperateId(operateId);
			sysplOperate.setStatus(DISABLE);
			updateOperate(sysplOperate, request); //修改模块操作
			moduleService.deleteModOptRefByOperateId(operateId, request); //根据操作ID删除模块操作关系,级联删除权限
			writeLog(sysplOperate, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			
			return true;
		} else {
			SysplOperate sysplOperate = findOperatesByOperateId(operateId);
			sysplOperate.setStatus(ENABLE);
			updateOperate(sysplOperate, request); //修改模块操作
			writeLog(sysplOperate, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			
			return true;
		}
	}

	@Override
	public List<SysplOperate> findAllOperates() {
		return operateRepository.findAllOperates();
	}

	@Override
	public Page<SysplOperate> findOperatesByCon(String modOptName, Pageable page) {
		logger.debug("start=" + page.getOffset() + " limit=" + page.getPageSize() + " modOptName" + modOptName);
		
		return operateRepository.findOperatesByCon(modOptName, page);
	}

	@Override
	public boolean existsOperateByName(String moduleOptName) {
		int count = operateRepository.countOperatesByName(moduleOptName);
		
		logger.debug("find module total is " + count);
		
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public SysplOperate findOperatesByOperateId(Long operateId) {
		logger.debug("will find moduleopt id is " + operateId);
		
		return operateRepository.get(operateId);
	}

	@Transactional(readOnly=false)
	@Override
	public void updateOperate(SysplOperate operate, HttpServletRequest request) 
			throws ApplicationException {
		
		operateRepository.update(operate);
		
		writeLog(operate, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveOperate(SysplOperate operate, HttpServletRequest request) 
			throws ApplicationException {
		
		operateRepository.save(operate);
		
		writeLog(operate, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}

	
	// --------------------------- 私有方法-------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param sysplOperate  模块对象
	 * @param logLevel 日志级别
	 * @param opt 操作名称
	 * @param request 
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 1:17:12 PM
	 */
	private void writeLog(SysplOperate sysplOperate, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = sysplOperate.getOperateName() + SPLIT + sysplOperate.getOptImgLink() 
				+ SPLIT + sysplOperate.getOptFunLink() + SPLIT;
		sysOptLogService.saveLog(logLevel, SYS_MODOPT_MGR, opt, optContent, 
				sysplOperate.getOperateId() + "", request);
	}
	

	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setOperateRepository(SysplOperateRepository operateRepository) {
		this.operateRepository = operateRepository;
	}
	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
}
