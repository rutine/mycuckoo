package com.mycuckoo.common.constant;

/**
 * 功能说明: 操作名称(日志功能设置)
 *
 * @author rutine
 * @time Jul 2, 2017 10:29:40 AM
 * @version 3.0.0
 */
public enum OptNameEnum {
	SAVE("保存"),
	MODIFY("修改"),
	DELETE("删除"),
	ENABLE("启用"),
	DISABLE("停用"),
	START_SCHEDULER("启动调度器"),
	STOP_SCHEDULER("停止调度器"),
	START_JOB("启动job"),
	STOP_JOB("停止job"),
	USER_LOGIN("用户登录"),
	RESET_PWD("重置密码");

	
	private String name;
	
	OptNameEnum(String name) {
		this.name = name;
	}
	
	public String value() { return name; }
}
