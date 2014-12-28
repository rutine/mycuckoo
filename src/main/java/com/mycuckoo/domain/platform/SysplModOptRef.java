package com.mycuckoo.domain.platform;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:04:18 PM
 * @version 2.0.0
 */
public class SysplModOptRef implements Serializable {

	private Long modOptId;
	private SysplOperate sysplOperate;
	private SysplModuleMemu sysplModuleMemu;

	/** default constructor */
	public SysplModOptRef() {
	}

	/** minimal constructor */
	public SysplModOptRef(Long modOptId) {
		this.modOptId = modOptId;
	}

	/** full constructor */
	public SysplModOptRef(Long modOptId, SysplOperate sysplOperate,
			SysplModuleMemu sysplModuleMemu) {
		this.modOptId = modOptId;
		this.sysplOperate = sysplOperate;
		this.sysplModuleMemu = sysplModuleMemu;
	}

	public Long getModOptId() {
		return this.modOptId;
	}

	public void setModOptId(Long modOptId) {
		this.modOptId = modOptId;
	}

	public SysplOperate getSysplOperate() {
		return this.sysplOperate;
	}

	public void setSysplOperate(SysplOperate sysplOperate) {
		this.sysplOperate = sysplOperate;
	}
//	@JSON(serialize=false)
	public SysplModuleMemu getSysplModuleMemu() {
		return this.sysplModuleMemu;
	}

	public void setSysplModuleMemu(SysplModuleMemu sysplModuleMemu) {
		this.sysplModuleMemu = sysplModuleMemu;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		SysplModOptRef sysplModOptRef = (SysplModOptRef)obj;
		if(sysplModOptRef.getModOptId() != null && modOptId != null && 
			sysplModOptRef.getModOptId().longValue() ==
			this.getModOptId().longValue()){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (modOptId == null ? 0 : modOptId.hashCode());

		return result;
	}
}
