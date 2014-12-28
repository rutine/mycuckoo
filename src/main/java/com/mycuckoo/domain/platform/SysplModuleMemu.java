package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:04:59 PM
 * @version 2.0.0
 */
public class SysplModuleMemu implements Serializable, Comparator<SysplModuleMemu>  {
	
	private Long moduleId; //模块ID
	private SysplModuleMemu sysplModuleMemu; //上级模块
	private String modName; //模块名称
	private String modEnId; //模块英文ID
	private String modImgCls; //模块图片样式
	private String modLevel; //模块级别
	private Integer modOrder; //模块顺序
	private String status; 
	private String memo; //备注
	private String creator; //创建人
	private Date createDate; //创建时间
	private List<SysplModuleMemu> sysplModuleMemus = Lists.newArrayList();
	
	private Long upModId;
	private String upModName; //上级模块名称
	private boolean isLeaf;
	private String optFunLink;//为操作准备功能链接
	private String belongToSys;//所属系统
	private String modPageType;//页面类型

	/** default constructor */
	public SysplModuleMemu() {
	}

	/** minimal constructor */
	public SysplModuleMemu(Long moduleId) {
		this.moduleId = moduleId;
	}

	/** full constructor */
	public SysplModuleMemu(Long moduleId, SysplModuleMemu sysplModuleMemu,
			String modName, String modEnId, String modImgCls, String modLevel,
			Integer modOrder,  String status,
			String memo, String creator, Date createDate, List<SysplModuleMemu> sysplModuleMemus) {
		this.moduleId = moduleId;
		this.sysplModuleMemu = sysplModuleMemu;
		this.modName = modName;
		this.modEnId = modEnId;
		this.modImgCls = modImgCls;
		this.modLevel = modLevel;
		this.modOrder = modOrder;
		this.status = status;
		this.memo = memo;
		this.creator = creator;
		this.createDate = createDate;
		this.sysplModuleMemus = sysplModuleMemus;
	}

	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
//	@JSON(serialize=false)
	public SysplModuleMemu getSysplModuleMemu() {
		return this.sysplModuleMemu;
	}

	public void setSysplModuleMemu(SysplModuleMemu sysplModuleMemu) {
		this.sysplModuleMemu = sysplModuleMemu;
	}

	public String getModName() {
		return this.modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public String getModEnId() {
		return this.modEnId;
	}

	public void setModEnId(String modEnId) {
		this.modEnId = modEnId;
	}

	public String getModImgCls() {
		return this.modImgCls;
	}

	public void setModImgCls(String modImgCls) {
		this.modImgCls = modImgCls;
	}

	public String getModLevel() {
		return this.modLevel;
	}

	public void setModLevel(String modLevel) {
		this.modLevel = modLevel;
	}

	public Integer getModOrder() {
		return this.modOrder;
	}

	public void setModOrder(Integer modOrder) {
		this.modOrder = modOrder;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
//	@JSON(serialize=false)
	public List<SysplModuleMemu> getSysplModuleMemus() {
		return this.sysplModuleMemus;
	}

	public void setSysplModuleMemus(List<SysplModuleMemu> sysplModuleMemus) {
		this.sysplModuleMemus = sysplModuleMemus;
	}
	
	@Override
	public int compare(SysplModuleMemu sysplModuleMemu1, SysplModuleMemu sysplModuleMemu2) {
		return sysplModuleMemu1.getModuleId().compareTo(sysplModuleMemu2.getModuleId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(this.getClass() != obj.getClass()) return false;
		SysplModuleMemu sysplModuleMemu = (SysplModuleMemu)obj;
		if(moduleId != null && sysplModuleMemu.getModuleId() != null &&
			this.moduleId.longValue() == sysplModuleMemu.getModuleId().longValue()){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (moduleId == null ? 0 : moduleId.hashCode());

		return result;
	}

	public Long getUpModId() {
		return upModId;
	}

	public void setUpModId(Long upModId) {
		this.upModId = upModId;
	}

	public String getUpModName() {
		return upModName;
	}

	public void setUpModName(String upModName) {
		this.upModName = upModName;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getOptFunLink() {
		return optFunLink;
	}

	public void setOptFunLink(String optFunLink) {
		this.optFunLink = optFunLink;
	}

	public String getBelongToSys() {
		return belongToSys;
	}

	public void setBelongToSys(String belongToSys) {
		this.belongToSys = belongToSys;
	}

	public String getModPageType() {
		return modPageType;
	}

	public void setModPageType(String modPageType) {
		this.modPageType = modPageType;
	}
}
