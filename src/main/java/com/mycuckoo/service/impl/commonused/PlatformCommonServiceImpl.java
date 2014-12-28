package com.mycuckoo.service.impl.commonused;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.platform.SysplSysParameter;
import com.mycuckoo.service.iface.commonused.PlatformCommonService;
import com.mycuckoo.service.iface.platform.DistrictService;
import com.mycuckoo.service.iface.platform.ModuleService;
import com.mycuckoo.service.iface.platform.SystemParameterService;

/**
 * 功能说明: 系统平台对外的公共业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:10:12 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class PlatformCommonServiceImpl implements PlatformCommonService {

	private ModuleService moduleService;
	private SystemParameterService systemParameterService;
//	private DictionaryService dictionaryService;
	private DistrictService districtService;
//	private CodeService codeService;
	
	@Override
	public Map filterModuleMenu(List<SysplModuleMemu> list) {
		return moduleService.filterModule(list);
	}

	@Override
	public List<SysplModOptRef> findAllModOptRefs() {
		return moduleService.findAllModOptRefs();
	}

	@Override
	public List<SysplModuleMemu> findAllModules() {
		return moduleService.findAllModules();
	}

	@Override
	public SysplDistrict getDistrictByDistrictId(Long districtId) {
		return districtService.getDistrictByDistrictId(districtId);
	}

	@Override
	public List<SysplModOptRef> findModOptRefByModOptRefIds(List<Long> modOptRefIdList) {
		return moduleService.findModOptRefsByModOptRefIds(modOptRefIdList.toArray(new Long[modOptRefIdList.size()]));
	}

	@Override
	public String findSystemParaByParaName(String paraName) {
		SysplSysParameter sysplSysParameter = systemParameterService.getSystemParameterByParaName(paraName);
		return sysplSysParameter == null ? "" : sysplSysParameter.getParaValue();
	}


	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	@Autowired
	public void setSystemParameterService(
		SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}
//	@Autowired
//	public void setDictionaryService(DictionaryService dictionaryService) {
//		this.dictionaryService = dictionaryService;
//	}
	@Autowired
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
//	@Autowired
//	public void setCodeService(CodeService codeService) {
//		this.codeService = codeService;
//	}

}
