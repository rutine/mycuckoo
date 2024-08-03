package com.mycuckoo.constant.enums;

/**
 * 功能说明: 模块名称(日志功能设置)
 *
 * @author rutine
 * @version 4.0.0
 * @time May 1, 2024 8:55:40 AM
 */
public enum ModuleName {
    SYS_MOD_MGR(1, "系统模块管理"),
    SYS_OPT_MGR(2, "系统模块操作管理"),
    SYS_RESOURCE_MRG(3, "系统资源管理"),
    SYS_CONFIG_MGR(4, "系统配置管理"),
    SYS_AFFICHE(5, "系统公告管理"),
    SYS_ACCESSORY(6, "系统附件"),
    SYS_PARAMETER(7, "系统参数管理"),
    SYS_CODE(8, "系统编码管理"),
    SYS_SCHEDULER(9, "系统调度管理"),
    SYS_PRIVILEGE(10, "系统权限管理"),
    SYS_DISTRICT(11, "省市地区管理"),
    SYS_TYPEDIC(12, "类别字典管理"),

    ORGAN_MGR(13, "组织管理"),
    DEPT_MGR(14, "部门管理"),
    ROLE_MGR(15, "角色管理"),
    USER_MGR(16, "用户管理"),
    USER_LOGIN(17, "用户登录");


    public final int code;
    public final String title;

    ModuleName(int code, String title) {
        this.code = code;
        this.title = title;
    }
}
