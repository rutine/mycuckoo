package com.mycuckoo.web.vo;

/**
 * 功能说明:
 * 
 * @author rutine
 * @time Sep 23, 2014 10:54:56 AM
 * @version 2.0.0
 */
public class MainFrameFun {

	private String funId;
	private String funName;
	private String funURI;
	private String funURIDesc;
	private String funMemo;

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public String getFunURI() {
		return funURI;
	}

	public void setFunURI(String funURI) {
		this.funURI = funURI;
	}

	public String getFunMemo() {
		return funMemo;
	}

	public void setFunMemo(String funMemo) {
		this.funMemo = funMemo;
	}

	public String getFunId() {
		return funId;
	}

	public void setFunId(String funId) {
		this.funId = funId;
	}

	public String getFunURIDesc() {
		return funURIDesc;
	}

	public void setFunURIDesc(String funURIDesc) {
		this.funURIDesc = funURIDesc;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		MainFrameFun mainFrameFun = (MainFrameFun) obj;
		if (this.getFunURI().equals(mainFrameFun.getFunURI()))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		int hashcode = 7;

		hashcode = hashcode * 37 + (funURI == null ? 0 : funURI.hashCode());

		return hashcode;
	}
}
