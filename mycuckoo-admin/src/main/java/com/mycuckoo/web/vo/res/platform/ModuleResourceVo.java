package com.mycuckoo.web.vo.res.platform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * 功能说明: 模块菜单和模块操作列表
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/16 7:36
 */
public class ModuleResourceVo {
    private List<ModuleMenuVo> menu  = Lists.newArrayList(); // 模块菜单list
    private Map<Long, List<ResourceVo>> resource = Maps.newHashMap(); // 四级资源

    public ModuleResourceVo(List<ModuleMenuVo> menu, Map<Long, List<ResourceVo>> resource) {
        this.menu = menu;
        this.resource = resource;
    }

    public List<ModuleMenuVo> getMenu() {
        return menu;
    }

    public Map<Long, List<ResourceVo>> getResource() {
        return resource;
    }
}
