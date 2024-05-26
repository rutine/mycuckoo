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
public class RowSqlBuilder extends SqlBuilder {
    private RowResolver resolver;

    public RowSqlBuilder(PlainSelect select, int paramIndex, RowResolver resolver) {
        super(select, paramIndex);

        this.resolver = resolver;
    }


    protected void auth(BiConsumer<String, Object> consumer) {
        if (this.resolver == null) {
            return;
        }
        PrivilegeInfo info = PrivilegeContextHolder.get();
        if (info == null || info.isSkip()) {
            return;
        }

        if (resolver.isPreFiltered()) {
            consumer.accept("id", Integer.class);
            this.wheres.add(0, this.newEq(new net.sf.jsqlparser.schema.Column("id"), this.newJdbcParameter()));

            //组织行权限
            if (resolver.getDefaultAuth().getRow() == 1) {
                consumer.accept("orgId", info.getOrgId());
                this.wheres.add(this.newEq(new net.sf.jsqlparser.schema.Column(resolver.getDefaultAuth().getColumn()), this.newJdbcParameter()));
                return;
            }
        }
        this.processSelect(source, consumer, false);
    }


    private boolean processSelect(Select select, BiConsumer<String, Object> consumer, boolean filter) {
        if (select == null) {
            return false;
        }

        if (select instanceof SetOperationList) {
            SetOperationList operationList = (SetOperationList)select;
            for (Select plainSelect : operationList.getSelects()) {
                this.processSelect(plainSelect, consumer, filter);
            }
        } else if (select instanceof ParenthesedSelect) {
            this.processSelect(((ParenthesedSelect) select).getSelect(), consumer, filter);
        } else if (select instanceof PlainSelect){
            return this.processPlainSelect(select.getPlainSelect(), consumer, filter);
        }

        return false;
    }
    private boolean processPlainSelect(PlainSelect plainSelect, BiConsumer<String, Object> consumer, boolean justQuery) {
        Table table = null;
        boolean found = false;
        if (plainSelect.getFromItem() instanceof Table) {
            table = getTable((Table) plainSelect.getFromItem());
        } else if ((plainSelect.getJoins() == null || plainSelect.getJoins().isEmpty()) //没有join sql, 无法继续
                && plainSelect.getFromItem().getAlias() != null
                && (found = this.processFromItem(plainSelect.getFromItem(), consumer, justQuery))) {
            Alias alias = plainSelect.getFromItem().getAlias();
            PreAuthInfo authData = resolver.getProperty(alias.getName());
            if (authData != null && authData.getAlias() == null) {
                table = new Table(alias.getName());
            }
        }

        if (table != null) {
            if (justQuery) {
                return true;
            }

            if (plainSelect == this.source) {
                this.wheres.add(resolver.rowExpression(table, consumer));
            } else {
//                plainSelect.setWhere(this.newAnd(plainSelect.getWhere(), resolver.rowExpression(table, consumer)));
                plainSelect.addJoins(resolver.rowJoins(table, consumer));
            }
            return true;
        }

        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                if (join.getFromItem() instanceof Table) {
                    table = getTable((Table) join.getFromItem());
                } else if (join.getFromItem().getAlias() != null
                        && (found = this.processFromItem(join.getFromItem(), consumer, true))) {
                    table = new Table(join.getFromItem().getAlias().getName());
                }

                if (table != null) {
                    if (justQuery) {
                        return true;
                    }

                    if (plainSelect == this.source) {
                        this.wheres.add(resolver.rowExpression(table, consumer));
                    } else {
//                        plainSelect.setWhere(this.newAnd(plainSelect.getWhere(), resolver.rowExpression(table, consumer)));
                        plainSelect.addJoins(resolver.rowJoins(table, consumer));
                    }
                    return true;
                }
            }
        }

        if (!found && plainSelect == this.source) {
            found = this.processWhere(source.getWhere(), consumer);

            if (!found) {
                Table authTable = new Table(resolver.getPropertyNames()[0]);
                this.wheres.add(resolver.rowExpression(authTable, consumer));
                return true;
            }
        }

        return found;
    }
    private boolean processFromItem(FromItem fromItem, BiConsumer<String, Object> consumer, boolean justQuery) {
        if (fromItem == null) {
            return false;
        }

        if (fromItem instanceof ParenthesedFromItem) {
            ParenthesedFromItem parenthesedFromItem = (ParenthesedFromItem) fromItem;
            if (parenthesedFromItem.getJoins() != null) {
                for (Join join : parenthesedFromItem.getJoins()) {
                    boolean found = this.processFromItem(join.getFromItem(), consumer, true);
                    if (found) {
                        return true;
                    }
                }
            }
        } else if (fromItem instanceof Select) {
            Select select = (Select)fromItem;
            return this.processSelect(select, consumer, justQuery);
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