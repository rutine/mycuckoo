package com.mycuckoo.domain.vo;

import java.io.Serializable;

/**
 * 功能说明:
 * 
 * @author rutine
 * @time Sep 23, 2014 10:59:31 AM
 * @version 2.0.0
 */
public class TreeVoExtend extends TreeVo implements Serializable {
	private static final long serialVersionUID = 10000L;
	private boolean checked;
	private boolean nocheck;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

}
