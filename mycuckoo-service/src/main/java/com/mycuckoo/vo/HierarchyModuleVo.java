package com.mycuckoo.vo;

import java.util.List;
import java.util.Map;

import com.mycuckoo.vo.platform.ModuleMemuVo;

/**
 * 功能说明: 四级层级菜单模块
 *
 * @author rutine
 * @time Jul 2, 2017 6:17:29 PM
 * @version 3.0.0
 */
public class HierarchyModuleVo {
	private List<ModuleMemuVo> first;
	private Map<String, List<ModuleMemuVo>> second;
	private Map<String, List<ModuleMemuVo>> third;
	private Map<Long, List<ModuleMemuVo>> fourth;
	private List<ModuleMemuVo> commonFun;
	private String row;
	
	public HierarchyModuleVo(
			List<ModuleMemuVo> first, 
			Map<String, List<ModuleMemuVo>> second, 
			Map<String, List<ModuleMemuVo>> third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public List<ModuleMemuVo> getFirst() {
		return first;
	}
	public void setFirst(List<ModuleMemuVo> first) {
		this.first = first;
	}
	public Map<String, List<ModuleMemuVo>> getSecond() {
		return second;
	}
	public void setSecond(Map<String, List<ModuleMemuVo>> second) {
		this.second = second;
	}
	public Map<String, List<ModuleMemuVo>> getThird() {
		return third;
	}
	public void setThird(Map<String, List<ModuleMemuVo>> third) {
		this.third = third;
	}
	public Map<Long, List<ModuleMemuVo>> getFourth() {
		return fourth;
	}
	public void setFourth(Map<Long, List<ModuleMemuVo>> fourth) {
		this.fourth = fourth;
	}
	public List<ModuleMemuVo> getCommonFun() {
		return commonFun;
	}
	public void setCommonFun(List<ModuleMemuVo> commonFun) {
		this.commonFun = commonFun;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
}
