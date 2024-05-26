package com.mycuckoo.core.repository.param;

/**
 * 功能说明: 聚合列参数
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:07
 */
public class AggrParam extends ColumnAdapter {
    private String function;

    public AggrParam(Column column, String function) {
        super(column);
        this.function = function;
    }

    public String toString() {
        return this.function.toUpperCase();
    }
}
