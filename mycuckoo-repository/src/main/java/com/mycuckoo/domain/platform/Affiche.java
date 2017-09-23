package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 8:59:44 PM
 * @version 3.0.0
 */
public class Affiche implements Serializable {

	private Long afficheId; //公告ID
	private String afficheTitle; //公告标题
	private Date afficheInvalidate; //公告有效期
	private Short affichePulish; //是否发布
	private String afficheContent; //公告内容
	private List<Accessory> accessories; //公告附件添加

	/** default constructor */
	public Affiche() {
	}

	/** minimal constructor */
	public Affiche(Long afficheId) {
		this.afficheId = afficheId;
	}

	/** full constructor */
	public Affiche(Long afficheId, String afficheTitle,
			Date afficheInvalidate, Short affichePulish, String afficheContent) {
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

	public List<Accessory> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<Accessory> accessories) {
		this.accessories = accessories;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
