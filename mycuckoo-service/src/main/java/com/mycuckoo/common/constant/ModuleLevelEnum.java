package com.mycuckoo.common.constant;

/**
 * 功能说明: 菜单模块级别
 *
 * @author rutine
 * @time Jul 2, 2017 5:54:33 PM
 * @version 3.0.0
 */
public enum ModuleLevelEnum {
	ONE(1, "一级菜单"),
	TWO(2, "二级菜单"),
	THREE(3, "三级菜单");
	
	private Integer level;
	private String desc;
	
	ModuleLevelEnum(Integer level, String desc) {
		this.level = level;
		this.desc = desc;
	}
	
	public Integer value() {return level;}
	
	public static ModuleLevelEnum value(String level) {
		ModuleLevelEnum[] levels = ModuleLevelEnum.values();
		for(ModuleLevelEnum levelEnum : levels) {
			if(levelEnum.value().equals(level)) {
				return levelEnum;
			}
		}
		
		return null;
	}
}
