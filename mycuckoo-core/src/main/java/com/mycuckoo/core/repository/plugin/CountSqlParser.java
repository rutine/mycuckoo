package com.mycuckoo.core.repository.plugin;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.ParenthesedFromItem;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明: 参考https://github.com/pagehelper/Mybatis-PageHelper
 *
 * @author rutine
 * @version 3.0.0
 * @time Jun 18, 2017 11:02:42 AM
 */
public class CountSqlParser {
    public static final String KEEP_ORDERBY = "/*keep orderby*/";
    private static final Alias TABLE_ALIAS;

    static {
        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }

    /**
     * 获取智能的countSql
     *
     * @param sql
     * @return
     */
    public String getSmartCountSql(String sql) {
        return getSmartCountSql(sql, "0");
    }

    /**
     * 获取智能的countSql
     *
     * @param sql
     * @param name 列名，默认 0
     * @return
     */
    public String getSmartCountSql(String sql, String name) {
        // 解析SQL
        Statement stmt = null;
        // 特殊sql不需要去掉order by时，使用注释前缀
        if (sql.indexOf(KEEP_ORDERBY) >= 0) {
            return getSimpleCountSql(sql);
        }

        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (Throwable e) {
            // 无法解析的用一般方法返回count语句
            return getSimpleCountSql(sql);
        }

        Select select = (Select) stmt;
        try {
            // 处理body-去order by
            processSelectBody(select);
        } catch (Exception e) {
            // 当 sql 包含 group by 时，不去除 order by
            return getSimpleCountSql(sql);
        }

        // 处理with-去order by
        processWithItemsList(select.getWithItemsList());
        // 处理为count查询
        select = sqlToCount(select, name);
        String result = select.toString();
        return result;
    }

    /**
     * 获取普通的Count-sql
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql) {
        return getSimpleCountSql(sql, "0");
    }

    /**
     * 获取普通的Count-sql
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql, String name) {
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("select count(");
        stringBuilder.append(name);
        stringBuilder.append(") from (");
        stringBuilder.append(sql);
        stringBuilder.append(") tmp_count");
        return stringBuilder.toString();
    }

    /**
     * 将sql转换为count查询
     *
     * @param select
     */
    public Select sqlToCount(Select select, String name) {
        // 是否能简化count查询
        List<SelectItem<?>> COUNT_ITEM = new ArrayList<>();
        COUNT_ITEM.add(new SelectItem(new Column("count(" + name + ")")));
        if (select instanceof PlainSelect && isSimpleCount(select.getPlainSelect())) {
            select.getPlainSelect().setSelectItems(COUNT_ITEM);
            return select;
        } else {
            ParenthesedSelect subSelect = new ParenthesedSelect();
            subSelect.withSelect(select);
            subSelect.setAlias(TABLE_ALIAS);

            PlainSelect plainSelect = new PlainSelect();
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            return plainSelect;
        }
    }

    /**
     * 是否可以用简单的count查询方式
     *
     * @param select
     * @return
     */
    public boolean isSimpleCount(PlainSelect select) {
        // 包含group by的时候不可以
        if (select.getGroupBy() != null) {
            return false;
        }
        // 包含distinct的时候不可以
        if (select.getDistinct() != null) {
            return false;
        }
        for (SelectItem item : select.getSelectItems()) {
            // select列中包含参数的时候不可以，否则会引起参数个数错误
            if (item.toString().contains("?")) {
                return false;
            }
            // 如果查询列中包含函数，也不可以，函数可能会聚合列
            if (item.getExpression() instanceof Function) {
                return false;
            }
        }
        return true;
    }

    /**
     * 处理selectBody去除Order by
     *
     * @param select
     */
    public void processSelectBody(Select select) {
        if (select instanceof SetOperationList) {
            SetOperationList operationList = (SetOperationList) select;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                List<Select> plainSelects = operationList.getSelects();
                for (Select plainSelect : plainSelects) {
                    processSelectBody(plainSelect);
                }
            }
            if (!orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements(null);
            }
        } else {
            processPlainSelect(select.getPlainSelect());
        }
    }

    /**
     * 处理PlainSelect类型的selectBody
     *
     * @param plainSelect
     */
    public void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    /**
     * 处理WithItem
     *
     * @param withItemsList
     */
    public void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList == null) {
            return;
        }
        for (WithItem item : withItemsList) {
            processSelectBody(item.getPlainSelect());
        }
    }

    /**
     * 处理子查询
     *
     * @param fromItem
     */
    public void processFromItem(FromItem fromItem) {
        if (fromItem instanceof ParenthesedFromItem) {
            ParenthesedFromItem subJoin = (ParenthesedFromItem) fromItem;
            if (subJoin.getJoins() != null) {
                for (Join join : subJoin.getJoins()) {
                    processFromItem(join.getRightItem());
                }
            }
        } else if (fromItem instanceof Select) {
            processSelectBody((Select) fromItem);
        }
        // Table时不用处理
    }

    /**
     * 判断Orderby是否包含参数，有参数的不能去
     *
     * @param orderByElements
     * @return
     */
    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }
}
