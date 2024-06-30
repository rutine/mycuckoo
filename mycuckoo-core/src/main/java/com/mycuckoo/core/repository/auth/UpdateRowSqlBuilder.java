package com.mycuckoo.core.repository.auth;

import com.mycuckoo.core.repository.param.PreAuthInfo;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.ParenthesedFromItem;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/28 20:12
 */
public class UpdateRowSqlBuilder  {

    private Statement source;
    private int paramIndex;
    private RowResolver resolver;
    private List<Expression> wheres;


    public UpdateRowSqlBuilder(Statement stmt, int paramIndex, RowResolver resolver) {
        this.source = stmt;
        this.paramIndex = paramIndex;
        this.resolver = resolver;
        this.wheres = new ArrayList<>();
    }

    public String build() {
        if (!wheres.isEmpty()) {
            Expression where = null;
            for (Expression expression : wheres) {
                where = this.newAnd(where, expression);
            }

            if (source instanceof Update) {
                ((Update) source).setWhere(this.newAnd(((Update) source).getWhere(), where));
            } else if (source instanceof Delete) {
                ((Delete) source).setWhere(this.newAnd(((Delete) source).getWhere(), where));
            }
        }

        return source.toString();
    }


    protected void row(BiConsumer<String, Object> consumer) {
        if (this.resolver == null) {
            return;
        }
        RowInfo info = RowContextHolder.get();
        if (info == null || info.isSkip()) {
            return;
        }

        if (source instanceof Update) {
            this.processUpdate((Update) source, consumer, false);
        } else if (source instanceof Delete) {
            this.processDelete((Delete) source, consumer, false);
        }
    }

    //不处理子查询, 对于update语句, 非面向sql编程, 不应存在复杂的业务sql
    private boolean processUpdate(Update update, BiConsumer<String, Object> consumer, boolean lookup) {
        boolean found = false;
        Table table = getTable(update.getTable());
        if (table == null && (update.getStartJoins() != null && !update.getStartJoins().isEmpty())) {
            for (Join join : update.getStartJoins()) {
                if (join.getFromItem() instanceof Table) {
                    table = getTable((Table) join.getFromItem());
                }
//                else if (join.getFromItem().getAlias() != null && (found = this.processFromItem(join.getFromItem(), consumer, lookup))) {
//                    table = new Table(join.getFromItem().getAlias().getName());
//                }

                if (table != null) {
                    break;
                }
            }
        }
        if (table == null && (update.getJoins() != null && !update.getJoins().isEmpty())) {
            for (Join join : update.getJoins()) {
                if (join.getFromItem() instanceof Table) {
                    table = getTable((Table) join.getFromItem());
                }
//                else if (join.getFromItem().getAlias() != null && (found = this.processFromItem(join.getFromItem(), consumer, lookup))) {
//                    table = new Table(join.getFromItem().getAlias().getName());
//                }

                if (table != null) {
                    break;
                }
            }
        }

        //递归处理中未找到, 当前默认
        if (table ==  null && update == this.source && !found) {
            table = new Table(resolver.getPropertyNames()[0]);
        } else if (table != null) {
            found = true;
        }

        if (table != null && !lookup) {
            if (update == this.source) {
                this.wheres.add(resolver.rowExpression(table, false, consumer));
            } else {
                update.addJoins(resolver.rowJoins(table, consumer));
                this.wheres.add(resolver.rowExpression(table, true, consumer));
            }
        }

        return found;
    }

    //不处理子查询, 对于delete语句, 非面向sql编程, 不应存在复杂的业务sql
    private boolean processDelete(Delete delete, BiConsumer<String, Object> consumer, boolean lookup) {
        boolean found = true;
        Table table = getTable(delete.getTable());
        if (table == null && (delete.getJoins() != null && !delete.getJoins().isEmpty())) {
            for (Join join : delete.getJoins()) {
                if (join.getFromItem() instanceof Table) {
                    table = getTable((Table) join.getFromItem());
                }
//                else if (join.getFromItem().getAlias() != null && this.processFromItem(join.getFromItem(), consumer, lookup)) {
//                    table = new Table(join.getFromItem().getAlias().getName());
//                }

                if (table != null) {
                    break;
                }
            }
        }
        if (table == null && delete.getTable().getAlias() != null) {
            Alias alias = delete.getTable().getAlias();
            PreAuthInfo authData = resolver.getProperty(alias.getName());
            if (authData != null && (authData.getAlias() == null || authData.getAlias().equals(""))) {
                table = new Table(alias.getName());
            }
        }

        //递归处理中未找到, 当前默认
        if (table ==  null && delete == this.source && !found) {
            table = new Table(resolver.getPropertyNames()[0]);
        } else if (table != null) {
            found = true;
        }

        if (table != null && !lookup) {
            if (delete == this.source) {
                this.wheres.add(resolver.rowExpression(table, false, consumer));
            } else {
                delete.addJoins(resolver.rowJoins(table, consumer));
                this.wheres.add(resolver.rowExpression(table, true, consumer));
            }
        }

        return found;
    }


    Expression newAnd(Expression left, Expression right) {
        if (left != null && right != null) {
            return new AndExpression(left, right);
        }

        return left != null ? left : right;
    }


    private boolean processFromItem(FromItem fromItem, BiConsumer<String, Object> consumer, boolean lookup) {
        if (fromItem == null) {
            return false;
        }

        if (fromItem instanceof ParenthesedFromItem) {
            ParenthesedFromItem parenthesedFromItem = (ParenthesedFromItem) fromItem;
            if (parenthesedFromItem.getJoins() != null) {
                for (Join join : parenthesedFromItem.getJoins()) {
                    if (this.processFromItem(join.getFromItem(), consumer, lookup)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    private Table getTable(Table table) {
        PreAuthInfo authData = resolver.getProperty(table.getName());
        if (authData == null) {
            return null;
        }
        if (authData.getAlias() == null || (table.getAlias() != null && table.getAlias().getName().equals(authData.getAlias()))) {
            return table;
        }

        return null;
    }
}