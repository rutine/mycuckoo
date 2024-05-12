package com.mycuckoo.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import com.mycuckoo.vo.platform.ResourceVo;

import java.util.List;
import java.util.Map;

/**
 * 功能说明: 模块菜单和模块操作列表
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 8, 2017 10:45:34 AM
 */
public class ModuleResourceVo {
    private List<ModuleMenuVo> menu  = Lists.newArrayList(); // 模块菜单list
    private Map<Long, List<ResourceVo>> resource = Maps.newHashMap(); // 四级资源

    public ModuleResourceVo() {

    }

    public ModuleResourceVo(List<ModuleMenuVo> menu, Map<Long, List<ResourceVo>> resource) {
        this.menu = menu;
        this.resource = resource;
    }

    public List<ModuleMenuVo> getMenu() {
        return menu;
    }

    public void setMenu(List<ModuleMenuVo> menu) {
        this.menu = menu;
    }

    public Map<Long, List<ResourceVo>> getResource() {
        return resource;
    }

    public void setResource(Map<Long, List<ResourceVo>> resource) {
        this.resource = resource;
    }
}
