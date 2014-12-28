package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.ORGAN_NAME;
import static com.mycuckoo.common.constant.Common.ROLE_NAME;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.Common.USER_CODE;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_CODE;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplCode;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplCodeRepository;
import com.mycuckoo.service.iface.platform.CodeService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 编码管理业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:25:41 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class CodeServiceImpl implements CodeService {
	
	static Logger logger = LoggerFactory.getLogger(CodeServiceImpl.class);
	private SysplCodeRepository codeRepository;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableCode(long codeId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			SysplCode sysplCode = getCodeByCodeId(codeId);
			sysplCode.setStatus(DISABLE);
			updateCode(sysplCode, request);
			
			String optContent = "编码停用：编码英文名称：" + sysplCode.getCodeEngName() + SPLIT
				+ "编码中文名称: " + sysplCode.getCodeName()  + SPLIT
				+ "编码所属模块名称: " + sysplCode.getCodeName() +SPLIT
				+ "编码效果: " + sysplCode.getCodeEffect() + SPLIT;
			sysOptLogService.saveLog(LOG_LEVEL_SECOND, SYS_CODE, DISABLE_DIS, optContent, 
					sysplCode.getCodeId().toString(), request);
			
			return true;
		} else {
			SysplCode sysplCode = getCodeByCodeId(codeId);
			sysplCode.setStatus(ENABLE);
			updateCode(sysplCode, request);
			
			String optContent = "编码启用：编码英文名称：" + sysplCode.getCodeEngName() + SPLIT
				+ "编码中文名称: " + sysplCode.getCodeName()  + SPLIT
				+ "编码所属模块名称: " + sysplCode.getCodeName() +SPLIT
				+ "编码效果: " + sysplCode.getCodeEffect() + SPLIT;
			sysOptLogService.saveLog(LOG_LEVEL_SECOND, SYS_CODE, ENABLE_DIS, optContent, 
					sysplCode.getCodeId().toString(), request);
		}
		
		return true;
	}

	@Override
	public Page<SysplCode> findCodesByCon(String codeEngName, 
			String codeName, String moduleName, Pageable page) {
		
		return codeRepository.findCodesByCon(codeEngName, codeName, moduleName, page);
	}

	@Override
	public boolean existCodeByCodeEngName(String codeEngName) {
		int count = codeRepository.countCodesByCodeEngName(codeEngName);
		if(count > 0) return true;
		return false;
	}

	@Override
	public SysplCode getCodeByCodeId(Long codeId) {
		SysplCode sysplCode = codeRepository.get(codeId);
		
		List<String> partList = new ArrayList<String>();
		partList.add(sysplCode.getPart1());
		partList.add(sysplCode.getPart2());
		partList.add(sysplCode.getPart3());
		partList.add(sysplCode.getPart4());
		
		List<String> partConList = new ArrayList<String>();
		partConList.add(sysplCode.getPart1Con());
		partConList.add(sysplCode.getPart2Con());
		partConList.add(sysplCode.getPart3Con());
		partConList.add(sysplCode.getPart4Con());
		
		sysplCode.setPartList(partList);
		sysplCode.setPartConList(partConList);
		
		return sysplCode;
	}

	@Transactional(readOnly=false)
	@Override
	public void updateCode(SysplCode code, HttpServletRequest request) 
			throws ApplicationException {
		
		codeRepository.update(code);
		String optContent = "编码英文名称：" + code.getCodeEngName() + SPLIT
			+ "编码中文名称: " + code.getCodeName()  + SPLIT
			+ "编码所属模块名称: " + code.getCodeName() +SPLIT
			+ "编码效果: " + code.getCodeEffect() + SPLIT;
		
		sysOptLogService.saveLog(LOG_LEVEL_SECOND, SYS_CODE, MODIFY_OPT, optContent, 
				code.getCodeId().toString(), request);
	}

	/**
	 * <b>注意：</b>该方法存在问题没有解决, 暂待解决.
	 */
	@Override
	public String getCodeNameByCodeEngName(String codeEngName, HttpServletRequest request) {
//		SysplModuleCode sysplModuleCode = (SysplModuleCode) codeDao.getPojoById("com.bhtec.domain.pojo.platform.SysplModuleCode", codeEngName);
//		String currentCode = sysplModuleCode != null ? sysplModuleCode.getCodeContent() : ""; // obtain newwest code from DB.
	
		String currentCode = "";
		
		SysplCode sysplCode = codeRepository.getCodeByCodeEngName(codeEngName);
		String part1 = sysplCode.getPart1();
		String part1Con = sysplCode.getPart1Con();
		String part2 = sysplCode.getPart2();
		String part2Con = sysplCode.getPart2Con();
		String part3 = sysplCode.getPart3();
		String part3Con = sysplCode.getPart3Con();
		String part4 = sysplCode.getPart4();
		String part4Con = sysplCode.getPart4Con();
		String delimite = sysplCode.getDelimite();
	
		part1Con = this.getPartContent(part1, part1Con, currentCode, request, 1, delimite);
		part2Con = this.getPartContent(part2, part2Con, currentCode, request, 2, delimite);
		part3Con = this.getPartContent(part3, part3Con, currentCode, request, 3, delimite);
		part4Con = this.getPartContent(part4, part4Con, currentCode, request, 4, delimite);
		
		String newCode = "";
		int partNum = sysplCode.getPartNum();
		switch (partNum) {
		case 1:
			newCode = part1Con;
			break;
		case 2:
			newCode = part1Con + delimite + part2Con;
			break;
		case 3:
			newCode = part1Con + delimite + part2Con + delimite + part3Con;
			break;
		case 4:
			newCode = part1Con + delimite + part2Con + delimite + part3Con + delimite + part4Con;
			break;
		}
		logger.info("newCode is : " + newCode);
	
//		if ("".equals(currentCode)) {
//			SysplModuleCode newSysplModuleCode = new SysplModuleCode();
//			newSysplModuleCode.setCodeContent(newCode);
//			newSysplModuleCode.setCodeEngName(codeEngName);
//			codeRepository.save(newSysplModuleCode);
//		} else {
//			sysplModuleCode.setCodeContent(newCode);
//			codeRepository.update(sysplModuleCode);
//		}
	
		return newCode;
	}

	@Transactional(readOnly=false)
	@Override
	public void saveCode(SysplCode code, HttpServletRequest request) 
			throws ApplicationException {
		
		codeRepository.save(code);
		String optContent = "编码英文名称：" + code.getCodeEngName() + SPLIT
			+ "编码中文名称: " + code.getCodeName()  + SPLIT
			+ "编码所属模块名称: " + code.getCodeName() +SPLIT
			+ "编码效果: " + code.getCodeEffect() + SPLIT;
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, SYS_CODE, SAVE_OPT, optContent, 
				code.getCodeId().toString(), request);
	}
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 私有方法获得格式化后的编码
	 *
	 * @param part
	 * @param partCon
	 * @param currentCode
	 * @param request
	 * @param partFlag
	 * @param delimite
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 8:03:22 PM
	 */
	private String getPartContent(String part, String partCon, String currentCode, 
			HttpServletRequest request, int partFlag,  String delimite) {
		
		if (isNullOrEmpty(part) || isNullOrEmpty(partCon)) return "";
		if ("date".equalsIgnoreCase(part)) {
			SimpleDateFormat sf = new SimpleDateFormat(partCon);
			partCon = sf.format(new Date());
		} else if ("char".equalsIgnoreCase(part)) {
			// do nothing
		} else if ("number".equalsIgnoreCase(part)) {
			if (!isNullOrEmpty(currentCode)) {
				String[] partArr = currentCode.split(delimite);
				String numStr = partArr[partFlag - 1];
				int numLength = numStr.length();
				numStr = (Integer.parseInt(numStr) + 1) + "";
				int newNumLength = numStr.length();
				while (numLength > newNumLength) {
					numStr = "0" + numStr;
					newNumLength++;
				}
				partCon = numStr;
			}
		} else if ("sysPara".equalsIgnoreCase(part)) {
			if ("userName".equals(partCon)) {
				partCon = (String) request.getSession().getAttribute(USER_CODE);
			} else if ("roleUser".equals(partCon)) {
				partCon = (String) request.getSession().getAttribute(ROLE_NAME)
					+ "~"
					+ (String) request.getSession().getAttribute(USER_CODE);
			} else if ("organRoleUser".equals(partCon)) {
				partCon = (String) request.getSession().getAttribute(ORGAN_NAME)
					+ "~"
					+ (String) request.getSession().getAttribute(ROLE_NAME)
					+ "~"
					+ (String) request.getSession().getAttribute(USER_CODE);
			}
		}
		
		return partCon;
	}

	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setCodeRepository(SysplCodeRepository codeRepository) {
		this.codeRepository = codeRepository;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}
