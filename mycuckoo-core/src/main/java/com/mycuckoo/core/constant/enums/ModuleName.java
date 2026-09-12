package com.mycuckoo.core.constant.enums;

/**
 * 功能说明: 模块名称(日志功能设置)
 *
 * @author rutine
 * @version 4.0.0
 * @time May 1, 2024 8:55:40 AM
 */
public enum ModuleName {
    SYS_MOD_MGR(1, "系统模块"),
    SYS_OPT_MGR(2, "系统操作"),
    SYS_RESOURCE_MRG(3, "系统资源"),
    SYS_CONFIG_MGR(4, "系统配置"),
    SYS_AFFICHE(5, "系统公告"),
    SYS_ACCESSORY(6, "系统附件"),
    SYS_PARAMETER(7, "系统参数"),
    SYS_CODE(8, "系统编码"),
    SYS_SCHEDULER(9, "系统调度"),
    SYS_PRIVILEGE(10, "系统权限"),
    SYS_DISTRICT(11, "省市区"),
    SYS_TYPEDIC(12, "系统字典"),

    ORGAN_MGR(13, "组织机构"),
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
