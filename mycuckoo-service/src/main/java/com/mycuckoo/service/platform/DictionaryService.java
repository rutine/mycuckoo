package com.mycuckoo.service.platform;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_TYPEDIC;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.platform.DicBigType;
import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.DicBigTypeMapper;
import com.mycuckoo.repository.platform.DicSmallTypeMapper;

/**
 * 功能说明: 字典大小类业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:29:37 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class DictionaryService {
	
	@Autowired
	private DicBigTypeMapper dicBigTypeMapper;
	@Autowired
	private DicSmallTypeMapper dicSmallTypeMapper;
	@Autowired
	private SystemOptLogService sysOptLogService;
	

	@Transactional(readOnly=false)
	public boolean disEnableDicBigType(long bigTypeId, String disEnableFlag) throws ApplicationException {		
		if(DISABLE.equals(disEnableFlag)) {
			DicBigType dicBigType = getDicBigTypeByBigTypeId(bigTypeId);
			dicBigTypeMapper.updateStatus(bigTypeId, DISABLE);
			writeLog(dicBigType, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
		} else {
			DicBigType dicBigType = getDicBigTypeByBigTypeId(bigTypeId);
			dicBigTypeMapper.updateStatus(bigTypeId, ENABLE);
			writeLog(dicBigType, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
		}
		
		return true;
	}

	public boolean existDicBigTypeByBigTypeCode(String bigTypeCode) {
		int count = dicBigTypeMapper.countByBigTypeCode(bigTypeCode);
		if(count > 0) return true;
		
		return false;
	}

	public List<DicSmallType> findDicSmallTypesByBigTypeCode(String bigTypeCode) {
		return dicSmallTypeMapper.findByBigTypeCode(bigTypeCode);
	}

	public DicBigType getDicBigTypeByBigTypeId(long bigTypeId) {
		return dicBigTypeMapper.get(bigTypeId);
	}

	public Page<DicBigType> findDicBigTypesByCon(Map<String, Object> params, Pageable page) {
		return dicBigTypeMapper.findByPage(params, page);
	}

	@Transactional(readOnly=false)
	public void updateDicBigType(DicBigType dicBigType) throws ApplicationException {
		dicSmallTypeMapper.deleteByBigTypeId(dicBigType.getBigTypeId());
		dicBigTypeMapper.update(dicBigType);
		
		writeLog(dicBigType, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	@Transactional(readOnly=false)
	public void saveDicBigType(DicBigType dicBigType) throws ApplicationException {
		dicBigTypeMapper.save(dicBigType);
		
		writeLog(dicBigType, LogLevelEnum.SECOND, OptNameEnum.SAVE);
	}
	
	@Transactional(readOnly=false)
	public void saveDicSmallTypes(List<DicSmallType> dicSmallTypes) throws ApplicationException {
		dicSmallTypes.forEach(dicSmallType -> {
			dicSmallTypeMapper.save(dicSmallType);
		});
	}

	// --------------------------- 私有方法-------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param dicBigType 字典大类
	 * @param logLevel 日志级别
	 * @param opt 操作名称
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 4:08:38 PM
	 */
	private void writeLog(DicBigType dicBigType, LogLevelEnum logLevel, OptNameEnum opt) throws ApplicationException {
		String optContent = "字典大类名称：" + dicBigType.getBigTypeName() + SPLIT;
		
		sysOptLogService.saveLog(logLevel, opt, SYS_TYPEDIC, optContent, dicBigType.getBigTypeId() + "");
	}
}
