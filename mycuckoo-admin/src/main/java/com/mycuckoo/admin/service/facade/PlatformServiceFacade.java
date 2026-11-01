package com.mycuckoo.admin.service.facade;

import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.admin.entity.platform.ModuleMenu;
import com.mycuckoo.admin.entity.platform.SysParameter;
import com.mycuckoo.admin.service.platform.DistrictService;
import com.mycuckoo.admin.service.platform.ModuleService;
import com.mycuckoo.admin.service.platform.SystemParameterService;
import com.mycuckoo.admin.entity.dto.platform.HierarchyModuleDTO;
import com.mycuckoo.admin.entity.dto.platform.ResourceDTO;
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


    public List<? extends SimpleTree> buildTree(List<ModuleMenu> menus, List<String> checkedOperations, boolean isCheckbox) {
        return moduleService.buildTree(menus, checkedOperations, isCheckbox);
    }

    public HierarchyModuleDTO filterModule(List<ModuleMenu> list) {
        return moduleService.filterModule(list);
    }


    public List<ResourceDTO> findAllModResRefs() {
        return moduleService.findAllModResRefs();
    }

    public List<ModuleMenu> findAllModule() {
        return moduleService.findAll();
    }

    public ModuleMenu getModule(Long moduleId) {
        return moduleService.get(moduleId);
    }

    public String findSystemParaByKey(String key) {
        SysParameter sysParameter = systemParameterService.getByKey(key);

        return sysParameter == null ? "" : sysParameter.getValue();
    }

}
