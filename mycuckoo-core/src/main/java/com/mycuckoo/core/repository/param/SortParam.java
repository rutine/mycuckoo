package com.mycuckoo.core.repository.param;

/**
 * 功能说明: 排序参数
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:27
 */
public class SortParam extends ColumnAdapter {
    private boolean asc;

    public SortParam(Column column, boolean asc) {
        super(column);
        this.asc = asc;
    }

    public boolean isAsc() {
        return asc;
    }

    public String toString() {
        return this.asc ? "asc" : "desc";
    }
}
