package com.mycuckoo.domain.vo;

import java.io.Serializable;

/**
 * 功能说明:
 * 
 * @author rutine
 * @time Sep 23, 2014 10:59:14 AM
 * @version 2.0.0
 */
public class TreeVo implements Serializable {

	private static final long serialVersionUID = 7272863933090319635L;

	private String id;
	private String parentId;
	private String text;
	private String icon; // 图片
	private String iconSkin; // css样式className
	private boolean leaf;
	private boolean isParent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
}
