package com.mycuckoo.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.vo.platform.ModuleMenuVo;

import java.util.List;
import java.util.Map;

/**
 * 功能说明: 模块菜单和模块操作列表
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 8, 2017 10:45:34 AM
 */
public class ModuleOperationVo {
    private List<ModuleMenuVo> moduleMenu = Lists.newArrayList(); // 模块菜单list
    private Map<Long, List<ModuleMenuVo>> moduleOperate = Maps.newHashMap(); // 四级模块操作

    public ModuleOperationVo() {

    }

    public ModuleOperationVo(List<ModuleMenuVo> moduleMenu, Map<Long, List<ModuleMenuVo>> moduleOperate) {
        this.moduleMenu = moduleMenu;
        this.moduleOperate = moduleOperate;
    }

    public List<ModuleMenuVo> getModuleMenu() {
        return moduleMenu;
    }

    public void setModuleMenu(List<ModuleMenuVo> moduleMenu) {
        this.moduleMenu = moduleMenu;
    }

    public Map<Long, List<ModuleMenuVo>> getModuleOperate() {
        return moduleOperate;
    }

    public void setModuleOperate(Map<Long, List<ModuleMenuVo>> moduleOperate) {
        this.moduleOperate = moduleOperate;
    }
}
