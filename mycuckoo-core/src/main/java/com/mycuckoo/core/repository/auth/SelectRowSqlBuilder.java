package com.mycuckoo.core.repository.auth;

import com.mycuckoo.core.repository.param.PreAuthInfo;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.function.BiConsumer;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:35
 */
public class SelectRowSqlBuilder extends SqlBuilder {
    private RowResolver resolver;

    public SelectRowSqlBuilder(PlainSelect select, int paramIndex, RowResolver resolver) {
        super(select, paramIndex);

        this.resolver = resolver;
    }


    protected void row(BiConsumer<String, Object> consumer) {
        if (this.resolver == null) {
            return;
        }
        RowInfo info = RowContextHolder.get();
        if (info == null || info.isSkip()) {
            return;
        }

        if (resolver.isPreFiltered()) {
            consumer.accept("id", Integer.class);
            this.wheres.add(0, this.newEq(new net.sf.jsqlparser.schema.Column("id"), this.newJdbcParameter()));

            //组织行权限
            if (resolver.getDefaultAuth().getRow() == 1) {
                consumer.accept("orgId", info.getOrgId());
                this.wheres.add(this.newEq(new net.sf.jsqlparser.schema.Column(resolver.getDefaultAuth().getTenant()), this.newJdbcParameter()));
                return;
            }
        }
        this.processSelect(source, consumer, false);
    }


    private boolean processSelect(Select select, BiConsumer<String, Object> consumer, boolean lookup) {
        if (select == null) {
            return false;
        }

        if (select instanceof SetOperationList) {
            SetOperationList operationList = (SetOperationList)select;
            for (Select plainSelect : operationList.getSelects()) {
                this.processSelect(plainSelect, consumer, lookup);
            }
        } else if (select instanceof ParenthesedSelect) {
            this.processSelect(((ParenthesedSelect) select).getSelect(), consumer, lookup);
        } else if (select instanceof PlainSelect){
            return this.processPlainSelect(select.getPlainSelect(), consumer, lookup);
        }

        return false;
    }
    private boolean processPlainSelect(PlainSelect plainSelect, BiConsumer<String, Object> consumer, boolean lookup) {
        Table table = null;
        boolean found = false;
        if (plainSelect.getFromItem() instanceof Table) {
            table = getTable((Table) plainSelect.getFromItem());
        }
        if (table == null && plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                if (join.getFromItem() instanceof Table) {
                    table = getTable((Table) join.getFromItem());
                } else if (join.getFromItem().getAlias() != null && (found = this.processFromItem(join.getFromItem(), consumer, true))) {
                    table = new Table(join.getFromItem().getAlias().getName());
                }

                if (table != null) {
                    break;
                }
            }
        }
        if (table == null && plainSelect.getFromItem().getAlias() != null
                && (found = this.processFromItem(plainSelect.getFromItem(), consumer, lookup))) {
            Alias alias = plainSelect.getFromItem().getAlias();
            PreAuthInfo authData = resolver.getProperty(alias.getName());
            if (authData != null && authData.getAlias() == null) {
                table = new Table(alias.getName());
            } else {
                return true;
            }
        }
        if (table == null && !found && plainSelect == this.source) {
            found = this.processWhere(source.getWhere(), consumer);
            if (!found) {
                table = new Table(resolver.getPropertyNames()[0]);
            }
        }

        if (table != null) {
            if (lookup) {
                return found;
            }

            if (plainSelect == this.source) {
                this.wheres.add(resolver.rowExpression(table, false, consumer));
            } else {
                plainSelect.addJoins(resolver.rowJoins(table, consumer));
                this.wheres.add(resolver.rowExpression(table, true, consumer));
            }
        }

        return found;
    }
    private boolean processFromItem(FromItem fromItem, BiConsumer<String, Object> consumer, boolean lookup) {
        if (fromItem == null) {
            return false;
        }

        if (fromItem instanceof ParenthesedFromItem) {
            ParenthesedFromItem parenthesedFromItem = (ParenthesedFromItem) fromItem;
            if (parenthesedFromItem.getJoins() != null) {
                for (Join join : parenthesedFromItem.getJoins()) {
                    if (this.processFromItem(join.getFromItem(), consumer, true)) {
                        return true;
                    }
                }
            }
        } else if (fromItem instanceof Select) {
            Select select = (Select)fromItem;
            return this.processSelect(select, consumer, lookup);
        }

        return false;
    }
    private boolean processWhere(Expression expression, BiConsumer<String, Object> consumer) {
        if (expression instanceof AndExpression) {
            AndExpression and = ((AndExpression) expression);
            if (processWhere(and.getLeftExpression(), consumer)) {
                return true;
            }
            if (processWhere(and.getRightExpression(), consumer)) {
                return true;
            }
        }
        else if (expression instanceof InExpression) {
            InExpression in = (InExpression) expression;
            if (in.getRightExpression() instanceof Select) {
                return processSelect(((Select) in.getRightExpression()), consumer, false);
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