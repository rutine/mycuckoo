package com.mycuckoo.core.repository.param;

/**
 * 功能说明: 过滤权限配置信息
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:13
 */
public class PreAuthInfo {
    private String table;
    private String alias;
    private String tenant;
    private String user;
    private Integer row; //行权限类型, 1: 组织 2: 用户

    public PreAuthInfo(String table, String alias, String tenant, String user) {
        this(table, alias, tenant, user, 1);
    }

    public PreAuthInfo(String table, String alias, String tenant, String user, int row) {
        this.table = table;
        this.alias = alias == null || "".equals(alias.trim()) ? null : alias.trim();
        this.tenant = tenant;
        this.user = user;
        this.row = row;
    }

    public String getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }

    public String getTenant() {
        return tenant;
    }

    public String getUser() {
        return user;
    }

    public Integer getRow() {
        return row;
    }
}
