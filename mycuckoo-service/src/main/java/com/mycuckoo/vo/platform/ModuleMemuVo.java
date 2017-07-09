package com.mycuckoo.vo.platform;

import com.mycuckoo.domain.platform.ModuleMemu;

/**
 * 功能说明: 模块菜单vo
 *
 * @author rutine
 * @time Jul 9, 2017 5:49:19 PM
 * @version 3.0.0
 */
public class ModuleMemuVo extends ModuleMemu {
	private String parentName; //上级模块名称
	private String optFunLink;//为操作准备功能链接
	private boolean isLeaf;
	
	/** default constructor */
	public ModuleMemuVo() {
	}

	/** minimal constructor */
	public ModuleMemuVo(Long moduleId) {
		super(moduleId);
	}
	
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getOptFunLink() {
		return optFunLink;
	}
	public void setOptFunLink(String optFunLink) {
		this.optFunLink = optFunLink;
	}
	public boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
}
