package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_TYPEDIC;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDicBigType;
import com.mycuckoo.domain.platform.SysplDicSmallType;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplDicBigTypeRepository;
import com.mycuckoo.persistence.iface.platform.SysplDicSmallTypeRepository;
import com.mycuckoo.service.iface.platform.DictionaryService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 字典大小类业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:29:37 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class DictionaryServiceImpl implements DictionaryService {
	
	private SysplDicBigTypeRepository dicBigTypeRepository;
	private SysplDicSmallTypeRepository dicSmallTypeRepository;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableDicBigType(long bigTypeId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			SysplDicBigType sysplDicBigType = getDicBigTypeByBigTypeId(bigTypeId);
			dicBigTypeRepository.updateDicBigTypeStatus(bigTypeId, DISABLE);
			writeLog(sysplDicBigType, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			return true;
		} else {
			SysplDicBigType sysplDicBigType = getDicBigTypeByBigTypeId(bigTypeId);
			dicBigTypeRepository.updateDicBigTypeStatus(bigTypeId, ENABLE);
			writeLog(sysplDicBigType, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public boolean existDicBigTypeByBigTypeCode(String bigTypeCode) {
		int count = dicBigTypeRepository.countDicBigTypesByBigTypeCode(bigTypeCode);
		if(count > 0) return true;
		
		return false;
	}

	@Override
	public List<SysplDicSmallType> findDicSmallTypesByBigTypeCode(String bigTypeCode) {
		return dicSmallTypeRepository.findDicSmallTypesByBigTypeCode(bigTypeCode);
	}

	@Override
	public SysplDicBigType getDicBigTypeByBigTypeId(long bigTypeId) {
		return dicBigTypeRepository.get(bigTypeId);
	}

	@Override
	public Page<SysplDicBigType> findDicBigTypesByCon(String bigTypeName, 
			String bigTypeCode, Pageable page) {
		
		return dicBigTypeRepository.findDicBigTypesByCon(bigTypeName, bigTypeCode, page);
	}

	@Transactional(readOnly=false)
	@Override
	public void updateDicBigType(SysplDicBigType dicBigType, 
			HttpServletRequest request) throws ApplicationException {
		
		dicSmallTypeRepository.deleteDicSmallTypesByBigTypeId(dicBigType.getBigTypeId());
		dicBigTypeRepository.update(dicBigType);
		
		writeLog(dicBigType, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveDicBigType(SysplDicBigType dicBigType, 
			HttpServletRequest request) throws ApplicationException {
		
		dicBigTypeRepository.save(dicBigType);
		
		writeLog(dicBigType, LOG_LEVEL_SECOND, SAVE_OPT, request);
	}
	
	@Transactional(readOnly=false)
	@Override
	public void saveDicSmallTypes(List<SysplDicSmallType> dicSmallTypes, 
			HttpServletRequest request) throws ApplicationException {
		
		dicSmallTypeRepository.save(dicSmallTypes);
	}

	// --------------------------- 私有方法-------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param dicBigType 字典大类
	 * @param logLevel 日志级别
	 * @param opt 操作名称
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 4:08:38 PM
	 */
	private void writeLog(SysplDicBigType dicBigType, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = "字典大类名称：" + dicBigType.getBigTypeName() + SPLIT;
		sysOptLogService.saveLog(logLevel, SYS_TYPEDIC, opt, optContent, dicBigType.getBigTypeId() + "", request);
	}
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setDicBigTypeRepository(
		SysplDicBigTypeRepository dicBigTypeRepository) {
		this.dicBigTypeRepository = dicBigTypeRepository;
	}
	@Autowired
	public void setDicSmallTypeRepository(
		SysplDicSmallTypeRepository dicSmallTypeRepository) {
		this.dicSmallTypeRepository = dicSmallTypeRepository;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	
}
