package com.mycuckoo.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 23, 2014 10:59:14 AM
 */
public class SimpleTree implements Serializable {
    private static final long serialVersionUID = 7272863933090319635L;

    private String id;
    private String parentId;
    private String text;
    private String icon; // 图片
    private String iconSkin; // css样式className
    private Boolean isLeaf;
    private List<? super SimpleTree> children;

    @Deprecated
    private Boolean leaf;
    @Deprecated
    private Boolean isParent;

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

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public List<? super SimpleTree> getChildren() {
        return children;
    }

    public void setChildren(List<? super SimpleTree> children) {
        this.children = children;
    }

    @Deprecated
    public Boolean isLeaf() {
        return leaf;
    }

    @Deprecated
    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    @Deprecated
    public Boolean getIsParent() {
        return isParent;
    }

    @Deprecated
    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
}
