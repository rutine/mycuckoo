package com.mycuckoo.vo;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.ModuleMemu;

/**
 * 功能说明: 模块菜单和模块操作列表
 *
 * @author rutine
 * @time Jul 8, 2017 10:45:34 AM
 * @version 3.0.0
 */
public class ModuleOperationVo {
	private List<ModuleMemu> moduleMenu = Lists.newArrayList(); // 模块菜单list
	private Map<Long, List<ModuleMemu>> moduleOperate = Maps.newHashMap(); // 四级模块操作
	
	public ModuleOperationVo() {
		
	}
	
	public ModuleOperationVo(List<ModuleMemu> moduleMenu, Map<Long, List<ModuleMemu>> moduleOperate) {
		this.moduleMenu = moduleMenu;
		this.moduleOperate = moduleOperate;
	}
	
	public List<ModuleMemu> getModuleMenu() {
		return moduleMenu;
	}
	public void setModuleMenu(List<ModuleMemu> moduleMenu) {
		this.moduleMenu = moduleMenu;
	}
	public Map<Long, List<ModuleMemu>> getModuleOperate() {
		return moduleOperate;
	}
	public void setModuleOperate(Map<Long, List<ModuleMemu>> moduleOperate) {
		this.moduleOperate = moduleOperate;
	}
}
