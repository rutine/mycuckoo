package com.mycuckoo.service.facade;

import com.mycuckoo.domain.platform.District;
import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.service.platform.DistrictService;
import com.mycuckoo.service.platform.ModuleService;
import com.mycuckoo.service.platform.SystemParameterService;
import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.web.vo.res.platform.ModuleMenuVo;
import com.mycuckoo.web.vo.res.platform.ResourceVo;
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


    public List<? extends SimpleTree> buildTree(List<ModuleMenuVo> menus, List<String> checkedOperations, boolean isCheckbox) {
        return moduleService.buildTree(menus, checkedOperations, isCheckbox);
    }

    public HierarchyModuleVo filterModule(List<ModuleMenuVo> list) {
        return moduleService.filterModule(list);
    }

    public List<ResourceVo> findAllModOptRefs() {
        return moduleService.findAllModOptRefs();
    }

    public List<ResourceVo> findAllModResRefs() {
        return moduleService.findAllModResRefs();
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

    public String findSystemParaByKey(String key) {
        SysParameter sysParameter = systemParameterService.getByKey(key);

        return sysParameter == null ? "" : sysParameter.getValue();
    }

}
