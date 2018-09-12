package com.mycuckoo.service.facade;

import com.mycuckoo.domain.platform.District;
import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.service.platform.DistrictService;
import com.mycuckoo.service.platform.ModuleService;
import com.mycuckoo.service.platform.SystemParameterService;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能说明: 系统平台对外的公共业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:10:12 AM
 */
@Service
@Transactional(readOnly = true)
public class PlatformServiceFacade {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private SystemParameterService systemParameterService;
    @Autowired
    private DistrictService districtService;


    public HierarchyModuleVo filterModule(List<ModuleMenuVo> list) {
        return moduleService.filterModule(list);
    }

    public List<ModOptRef> findAllModOptRefs() {
        return moduleService.findAllModOptRefs();
    }

    public List<ModuleMenuVo> findAllModule() {
        return moduleService.findAll();
    }

    public ModuleMenuVo getModule(Long moduleId) {
        return moduleService.get(moduleId);
    }

    public District getDistrict(Long districtId) {
        return districtService.get(districtId);
    }

    public List<ModOptRef> findModOptRefByModOptRefIds(List<Long> modOptRefIdList) {
        return moduleService.findModOptRefsByModOptRefIds(modOptRefIdList.toArray(new Long[modOptRefIdList.size()]));
    }

    public String findSystemParaByParaName(String paraName) {
        SysParameter sysParameter = systemParameterService.getByParaName(paraName);

        return sysParameter == null ? "" : sysParameter.getParaValue();
    }

}
