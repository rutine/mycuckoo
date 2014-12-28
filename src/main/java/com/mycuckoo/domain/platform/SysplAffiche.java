package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 8:59:44 PM
 * @version 2.0.0
 */
public class SysplAffiche implements Serializable {

	private Long afficheId; //公告ID
	private String afficheTitle; //公告标题
	private Date afficheInvalidate; //公告有效期
	private Short affichePulish; //是否发布
	private String afficheContent; //公告内容
	private List<SysplAccessory> accessoryList; //公告附件添加

	/** default constructor */
	public SysplAffiche() {
	}

	/** minimal constructor */
	public SysplAffiche(Long afficheId) {
		this.afficheId = afficheId;
	}

	/** full constructor */
	public SysplAffiche(Long afficheId, String afficheTitle,
			Date afficheInvalidate, Short affichePulish, String afficheContent,
			String afficheAccessory) {
		this.afficheId = afficheId;
		this.afficheTitle = afficheTitle;
		this.afficheInvalidate = afficheInvalidate;
		this.affichePulish = affichePulish;
		this.afficheContent = afficheContent;
	}

	public Long getAfficheId() {
		return this.afficheId;
	}

	public void setAfficheId(Long afficheId) {
		this.afficheId = afficheId;
	}

	public String getAfficheTitle() {
		return this.afficheTitle;
	}

	public void setAfficheTitle(String afficheTitle) {
		this.afficheTitle = afficheTitle;
	}

	public Date getAfficheInvalidate() {
		return this.afficheInvalidate;
	}

	public void setAfficheInvalidate(Date afficheInvalidate) {
		this.afficheInvalidate = afficheInvalidate;
	}

	public Short getAffichePulish() {
		return this.affichePulish;
	}

	public void setAffichePulish(Short affichePulish) {
		this.affichePulish = affichePulish;
	}

	public String getAfficheContent() {
		return this.afficheContent;
	}

	public void setAfficheContent(String afficheContent) {
		this.afficheContent = afficheContent;
	}

	public List<SysplAccessory> getAccessoryList() {
		return accessoryList;
	}

	public void setAccessoryList(List<SysplAccessory> accessoryList) {
		this.accessoryList = accessoryList;
	}
}
