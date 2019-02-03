package com.mycuckoo.vo.platform;

import com.mycuckoo.domain.platform.ModuleMenu;

/**
 * 功能说明: 模块菜单vo
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 9, 2017 5:49:19 PM
 */
public class ModuleMenuVo extends ModuleMenu {
    private String parentName; //上级模块名称
    private String optLink;//为操作准备功能链接
    private boolean isLeaf;

    /**
     * default constructor
     */
    public ModuleMenuVo() {
    }

    /**
     * minimal constructor
     */
    public ModuleMenuVo(Long moduleId) {
        super(moduleId);
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getOptLink() {
        return optLink;
    }

    public void setOptLink(String optLink) {
        this.optLink = optLink;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
}
