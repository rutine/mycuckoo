package com.mycuckoo.common.constant;

/**
 * 功能说明: 公共常量
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:09:47 PM
 */
public class Common {
    //web.xml web根目录名称
    public final static String WEB_APP_ROOT_KEY = "mycuckoo.root";

    //机构名称 ID
    public final static String ORGAN_NAME = "organName";
    public final static String ORGAN_ID = "organId";
    //角色名称 ID
    public final static String ROLE_NAME = "roleName";
    public final static String ROLE_ID = "roleId";
    public final static String ORGAN_ROLE_ID = "organRoleId";
    //用户名称 ID
    public final static String USER_PHOTO_URL = "userPhotoUrl";
    public final static String USER_NAME = "userName";
    public final static String USER_CODE = "userCode";
    public final static String USER_ID = "userId";

    //用户菜单
    public final static String MODULE_MENU = "moduleMenu";

    //日志功能设置
    public final static String SPLIT = ";";

    public final static String PRIVILEGE_TYPE_ROW = "row";
    public final static String PRIVILEGE_TYPE_OPT = "opt";
    public final static String PRIVILEGE_SCOPE = "privilegeScope";
    public final static String OWNER_TYPE_ROL = "rol";
    public final static String OWNER_TYPE_USR = "usr";
    public final static String USER_DEFAULT_PWD = "userdefaultpassword";
    public final static String[] PRIVILEGE_TYPE_ARR = {PRIVILEGE_TYPE_ROW, PRIVILEGE_TYPE_OPT};
    public final static String[] OWNER_TYPE_ARR = {OWNER_TYPE_USR, OWNER_TYPE_ROL};

    //创建系统文件路径
    public final static String MYCUCKOO_SYSTEM_FILE_DIR = "/home/rutine/upload";
}
