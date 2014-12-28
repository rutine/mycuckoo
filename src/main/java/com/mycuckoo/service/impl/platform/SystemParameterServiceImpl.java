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
import static com.mycuckoo.common.constant.ServiceVariable.SYS_PARAMETER;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysParameter;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplSysParameterRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.platform.SystemParameterService;

/**
 * 功能说明: 系统参数业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:55:50 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class SystemParameterServiceImpl implements SystemParameterService {
	
	private SysplSysParameterRepository sysParameterRepository;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableSystemParameter(long paraId, String disEnableFlag, 
			HttpServletRequest request)  throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			SysplSysParameter sysplSysParameter = getSystemParameterById(paraId);
			sysplSysParameter.setStatus(DISABLE);
			updateSystemParameter(sysplSysParameter, request);
			writeLog(sysplSysParameter, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			return true;
		} else {
			SysplSysParameter sysplSysParameter = getSystemParameterById(paraId);
			sysplSysParameter.setStatus(ENABLE);
			updateSystemParameter(sysplSysParameter, request);
			writeLog(sysplSysParameter, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public Page<SysplSysParameter> findSystemParametersByCon(String paraName, 
			String paraKeyName, Pageable page) {
		
		return sysParameterRepository.findSystemParametersByCon(paraName, paraKeyName, page);
	}

	@Override
	public SysplSysParameter getSystemParameterById(long paraId) {
		return sysParameterRepository.get(paraId);
	}

	@Override
	public SysplSysParameter getSystemParameterByParaName(String paraName) {
		return sysParameterRepository.getSystemParameterByParaName(paraName);
	}

	@Override
	@Transactional(readOnly=false)
	public void updateSystemParameter(SysplSysParameter systemParameter, 
			HttpServletRequest request) throws ApplicationException {
		
		sysParameterRepository.update(systemParameter);
		writeLog(systemParameter, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveSystemParameter(SysplSysParameter systemParameter, 
			HttpServletRequest request) throws ApplicationException {
		
		sysParameterRepository.save(systemParameter);
		writeLog(systemParameter, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}

	
	
	

	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param systemParameter 系统参数对象
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 8:20:17 PM
	 */
	private void writeLog(SysplSysParameter systemParameter, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = "参数名称：" + systemParameter.getParaName() + SPLIT 
			+ "参数键值：" + systemParameter.getParaKeyName() + SPLIT
			+ "参数值：" + systemParameter.getParaValue() + SPLIT
			+ "参数类型：" + systemParameter.getParaType();
		sysOptLogService.saveLog(logLevel, SYS_PARAMETER, opt, optContent, systemParameter.getParaId() + "", request);
	}
	
	
	
  
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setSysParameterRepository(SysplSysParameterRepository sysParameterRepository) {
		this.sysParameterRepository = sysParameterRepository;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}
