package com.mycuckoo.vo;

import java.util.List;
import java.util.Map;

import com.mycuckoo.domain.platform.ModuleMemu;

/**
 * 功能说明: 四级层级菜单模块
 *
 * @author rutine
 * @time Jul 2, 2017 6:17:29 PM
 * @version 3.0.0
 */
public class HierarchyModuleVo {
	private List<ModuleMemu> first;
	private Map<String, List<ModuleMemu>> second;
	private Map<String, List<ModuleMemu>> third;
	
	public HierarchyModuleVo(
			List<ModuleMemu> first, 
			Map<String, List<ModuleMemu>> second, 
			Map<String, List<ModuleMemu>> third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public List<ModuleMemu> getFirst() {
		return first;
	}
	public void setFirst(List<ModuleMemu> first) {
		this.first = first;
	}
	public Map<String, List<ModuleMemu>> getSecond() {
		return second;
	}
	public void setSecond(Map<String, List<ModuleMemu>> second) {
		this.second = second;
	}
	public Map<String, List<ModuleMemu>> getThird() {
		return third;
	}
	public void setThird(Map<String, List<ModuleMemu>> third) {
		this.third = third;
	}
}
