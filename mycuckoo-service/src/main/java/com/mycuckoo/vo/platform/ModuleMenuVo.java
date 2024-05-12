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
    private String id; //字符串类型, 方便操作
    private String parentName; //上级模块名称
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
        this.id = String.valueOf(moduleId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
}
