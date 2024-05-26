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
    private String column;
    private Integer row; //行权限类型, 1: 组织 2: 用户

    public PreAuthInfo(String table, String alias, String column) {
        this(table, alias, column, 2);
    }

    public PreAuthInfo(String table, String alias, String column, Integer row) {
        this.table = table;
        this.alias = alias == null || "".equals(alias.trim()) ? null : alias.trim();
        this.column = column;
        this.row = row == null ? 2 : row;
    }

    public String getTable() {
        return table;
    }

    public String getAlias() {
        return alias;
    }

    public String getColumn() {
        return column;
    }

    public Integer getRow() {
        return row;
    }
}
