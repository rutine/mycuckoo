package com.mycuckoo.vo;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.vo.platform.ModuleMemuVo;

/**
 * 功能说明: 模块菜单和模块操作列表
 *
 * @author rutine
 * @time Jul 8, 2017 10:45:34 AM
 * @version 3.0.0
 */
public class ModuleOperationVo {
	private List<ModuleMemuVo> moduleMenu = Lists.newArrayList(); // 模块菜单list
	private Map<Long, List<ModuleMemuVo>> moduleOperate = Maps.newHashMap(); // 四级模块操作
	
	public ModuleOperationVo() {
		
	}
	
	public ModuleOperationVo(List<ModuleMemuVo> moduleMenu, Map<Long, List<ModuleMemuVo>> moduleOperate) {
		this.moduleMenu = moduleMenu;
		this.moduleOperate = moduleOperate;
	}
	
	public List<ModuleMemuVo> getModuleMenu() {
		return moduleMenu;
	}
	public void setModuleMenu(List<ModuleMemuVo> moduleMenu) {
		this.moduleMenu = moduleMenu;
	}
	public Map<Long, List<ModuleMemuVo>> getModuleOperate() {
		return moduleOperate;
	}
	public void setModuleOperate(Map<Long, List<ModuleMemuVo>> moduleOperate) {
		this.moduleOperate = moduleOperate;
	}
}