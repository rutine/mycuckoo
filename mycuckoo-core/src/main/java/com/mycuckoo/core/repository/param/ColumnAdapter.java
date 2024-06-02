package com.mycuckoo.core.repository.param;

/**
 * 功能说明: 列属性适配类
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:05
 */
public abstract class ColumnAdapter implements Column {
    private Column column;

    public ColumnAdapter(Column column) {
        this.column = column;
    }


    @Override
    public String getType() {
        return column.getType();
    }

    @Override
    public String getField() {
        return column.getField();
    }

    @Override
    public String getTitle() {
        return column.getTitle();
    }

    @Override
    public String getFilterType() {
        return column.getFilterType();
    }

    @Override
    public Boolean isFilter() {
        return column.isFilter();
    }

    @Override
    public Boolean isBlank() {
        return column.isBlank();
    }

    @Override
    public Boolean isSort() {
        return column.isSort();
    }

    @Override
    public String getExtra() {
        return column.getExtra();
    }
}
