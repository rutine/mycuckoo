package com.mycuckoo.admin.entity.dto.platform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.admin.entity.platform.ModuleMenu;

import java.util.List;
import java.util.Map;

/**
 * 功能说明: 模块菜单和模块操作列表
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/16 7:36
 */
public class ModuleResourceDto {
    private List<ModuleMenu> menu  = Lists.newArrayList(); // 模块菜单list
    private Map<Long, List<ResourceDto>> resource = Maps.newHashMap(); // 四级资源

    public ModuleResourceDto(List<ModuleMenu> menu, Map<Long, List<ResourceDto>> resource) {
        this.menu = menu;
        this.resource = resource;
    }

    public List<ModuleMenu> getMenu() {
        return menu;
    }

    public Map<Long, List<ResourceDto>> getResource() {
        return resource;
    }
}
