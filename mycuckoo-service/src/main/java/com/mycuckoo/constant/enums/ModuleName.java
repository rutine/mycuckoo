package com.mycuckoo.constant.enums;

/**
 * 功能说明: 模块名称(日志功能设置)
 *
 * @author rutine
 * @version 4.0.0
 * @time May 1, 2024 8:55:40 AM
 */
public enum ModuleName {
    SYS_MOD_MGR("系统模块管理"),
    SYS_MODOPT_MGR("系统模块操作管理"),
    SYS_MODOPT_ASSIGN("模块分配操作"),
    SYS_CONFIG_MGR("系统配置管理"),
    SYS_AFFICHE("系统公告管理"),
    SYS_ACCESSORY("系统附件"),
    SYS_PARAMETER("系统参数管理"),
    SYS_CODE("系统编码管理"),
    SYS_SCHEDULER("系统调度管理"),
    SYS_MOD_ASSIGN_OPT("模块分配操作"),
    SYS_DISTRICT("省市地区管理"),
    SYS_TYPEDIC("类别字典管理"),

    ORGAN_MGR("机构管理"),
    ROLE_MGR("角色管理"),
    ROLE_ASSIGN("角色分配"),
    ROLE_CSS("rolemgr"),
    USER_MGR("用户管理"),
    USER_ROLE_MGR("用户分配角色");


    private String name;

    ModuleName(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
