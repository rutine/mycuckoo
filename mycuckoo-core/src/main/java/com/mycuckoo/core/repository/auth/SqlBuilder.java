package com.mycuckoo.core.repository.auth;

import com.mycuckoo.core.repository.param.AggrParam;
import com.mycuckoo.core.repository.param.ColumnResolver;
import com.mycuckoo.core.repository.param.WhereParam;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:32
 */
public class SqlBuilder {
    protected final static Table EMPTY_TABLE = new Table();

    protected PlainSelect source;
    protected ColumnResolver columnResolver;
    protected int paramIndex;

    protected List<SelectItem<?>> items;
    protected List<Expression> wheres;
    protected List<OrderByElement> sorts;

    public static void main(String[] args) throws JSQLParserException {
        String sql = "select (select * from stable1 st), t1.a, t2.b, concat(t1.a, 't') from table1, table2 left join table2 t2 on t2.id = t1.id where t1.a = ? and t1.b = t3.c";
        Statement stmt = CCJSqlParserUtil.parse(sql);

        TableStatement tableStatement = new TableStatement();
        tableStatement.setTable(new Table("table3"));
        WithItem withItem = new WithItem();
        withItem.withSelect(tableStatement)
                .withAlias(new Alias("T2"))
                .withLimit(new Limit().withByExpressions(new DoubleValue("1")))
                .withOffset(new Offset());

//        System.out.println(withItem.getPlainSelect());
        System.out.println(withItem instanceof Select);
        String newSql = new SqlBuilder((PlainSelect) stmt).subSql();
        System.out.println(stmt);
    }

    @Deprecated
    public SqlBuilder(PlainSelect select) {
        this(select, null);
    }

    public SqlBuilder(PlainSelect select, Integer paramIndex) {
        this.source = select;
        this.columnResolver = ColumnResolver.from(select.getSelectItems());
        this.paramIndex = paramIndex != null ? paramIndex : StringUtils.countOccurrencesOf(select.toString(), "?");
        this.items = new ArrayList<>();
        this.wheres = new ArrayList<>();
        this.sorts = new ArrayList<>();
    }

    public String build() {
        //存在不能识别字段所在table, fallback子查询
        if (columnResolver.isAmbiguous() && source.getJoins() != null && !source.getJoins().isEmpty()) {
            return subSql();
        }

        if (!items.isEmpty()) {
            source.setSelectItems(items);
        }

        Expression where = source.getWhere();
        for (Expression expression : wheres) {
            where = this.newAnd(where, expression);
        }
        source.setWhere(where);

        if (!sorts.isEmpty()) { //覆盖排序
            source.setOrderByElements(sorts);
        }

        return source.toString();
    }
    private String subSql() {
        Expression where = null;
        for (Expression expression : wheres) {
            where = this.newAnd(where, expression);
        }

        PlainSelect select = new PlainSelect();
        select.withSelectItems(items.isEmpty() ? Arrays.asList(new SelectItem(new AllColumns())) : items)
                .withFromItem(new ParenthesedSelect()
                        .withSelect(source)
                        .withAlias(new Alias("tmp")))
                .withWhere(where)
                .withOrderByElements(sorts);

        return select.toString();
    }

    protected void selectItem(AggrParam param) {
        Expression column = this.getColumn(param.getField());
        net.sf.jsqlparser.expression.Function function = new net.sf.jsqlparser.expression.Function().withName(param.toString()).withParameters(new ExpressionList(column));
        items.add(new SelectItem<>(function).withAlias(new Alias(param.getField())));
    }

    protected SqlBuilder distinct(String field) {
        Expression column = this.getColumn(field);
        SelectItem item = new SelectItem(column);
        item.setAlias(new Alias(field));

        //TODO 直接修改源SQL
        PlainSelect plainSelect = source;

        //重置查询字段
        plainSelect.setDistinct(new Distinct());
        plainSelect.setSelectItems(Arrays.asList(item));

        //增加xx is not null的条件，防止返回null的数据
        IsNullExpression isNull = this.newIsNull(column);
        isNull.setNot(true);
        this.wheres.add(isNull);

        this.sort(field, true);

        return this;
    }

    protected SqlBuilder like(WhereParam param, boolean search, BiConsumer<String, Object> consumer) {
        if (param.getText() == null) return this;

        String field = param.getField();
        Expression column = this.getColumn(field);

        // name like CONCAT('%', ?, '%')
        LikeExpression left = new LikeExpression();
        left.setLeftExpression(column);

        net.sf.jsqlparser.expression.Function function = new net.sf.jsqlparser.expression.Function();
        function.setName("CONCAT");
        left.setRightExpression(function);

        ExpressionList expressionList = new ExpressionList();
        function.setParameters(expressionList);
        if (search) {
            expressionList.addExpression(new StringValue("%"));
        }
        expressionList.addExpression(this.newJdbcParameter());
        expressionList.addExpression(new StringValue("%"));

        consumer.accept(String.format("wheres.%s", field), String.class);

        this.wheres.add(left);

//        Expression right = null;
//        if (param.useNull()) {
//            // name = '' or name is null
//            right = new OrExpression(this.newEqualsToEmpty(column), this.newIsNull(column));
//        }
//
//        this.wheres.add(this.newOr(left, right));

        return this;
    }

    protected SqlBuilder text(WhereParam param, BiConsumer<String, Object> consumer) {
        if (param.getText() == null) return this;

        String field = param.getField();
        Expression column = this.getColumn(field);

        // name = ?
        Expression left = this.newEq(column, this.newJdbcParameter());
        consumer.accept(String.format("wheres.%s", field), String.class);

        this.wheres.add(left);

        return this;
    }

    protected SqlBuilder scope(WhereParam param, BiConsumer<String, Object> consumer) {
        if (param.getValue() == null || (param.getStart() == null && param.getEnd() == null)) return this;

        String field = param.getField();
        Expression column = this.getColumn(field);

        //存在一个为空
        if (param.getStart() == null || param.getEnd() == null) {
            GreaterThanEquals greaterThanEquals = null;
            if (param.getStart() != null) {
                // name >= ?
                greaterThanEquals = this.newGE(column, this.newJdbcParameter());
                consumer.accept(String.format("wheres.%s.start", field), param.getStart().getClass());
            }

            MinorThanEquals minorThanEquals = null;
            if (param.getEnd() != null) {
                // name <= ?
                minorThanEquals = this.newLE(column, this.newJdbcParameter());
                consumer.accept(String.format("wheres.%s.end", field), param.getEnd().getClass());
            }

            // name >= ? || name <= ?
            Expression left = this.newAnd(greaterThanEquals, minorThanEquals);
            this.wheres.add(left);
            return this;
        }

        // name = ?
        Expression start = this.newJdbcParameter();
        consumer.accept(String.format("wheres.%s.start", field), param.getStart().getClass());
        // name = ?
        Expression end = this.newJdbcParameter();
        consumer.accept(String.format("wheres.%s.end", field), param.getEnd().getClass());
        // between ? and ?
        Expression left = this.newBetween(column, start, end);

        this.wheres.add(left);
        return this;
    }

    protected SqlBuilder multi(WhereParam param, BiConsumer<String, Object> consumer) {
        if (param.getValue() == null || param.getList() == null || param.getList().isEmpty()) return this;

        String field = param.getField();
        Expression column  = this.getColumn(field);
        List<Object> list = param.getList();
        List<Expression> parameters = new ArrayList<>();
        // name in (?, ?, ?)
        for (int i = 0; i < list.size(); ++i) {
            parameters.add(this.newJdbcParameter());
            consumer.accept(String.format("wheres_%s_%d", field, i), list.get(i));
        }

        ExpressionList expressionList = new ExpressionList(parameters);
        Expression left = new InExpression(column, expressionList);

        this.wheres.add(left);
        return this;
    }

    protected SqlBuilder district(WhereParam param, BiConsumer<String, Object> consumer) {
        if (param.getValue() == null || (param.getProvince() == null && param.getCity() == null && param.getArea() == null)) return this;

        String field = param.getField();
        EqualsTo province = null;
        if (param.getProvince() != null) {
            province = this.newEq(this.getColumn("province"), this.newJdbcParameter());
            consumer.accept(String.format("wheres.%s.province", field), String.class);
        }

        EqualsTo city = null;
        if (param.getCity() != null) {
            city = this.newEq(this.getColumn("city"), this.newJdbcParameter());
            consumer.accept(String.format("wheres.%s.city", field), String.class);
        }

        EqualsTo area = null;
        if (param.getArea() != null) {
            area = this.newEq(this.getColumn("area"), this.newJdbcParameter());
            consumer.accept(String.format("wheres.%s.area", field), String.class);
        }

        // province = ? and city = ? and area = ?
        Expression left = this.newAnd(this.newAnd(province, city), area);

        this.wheres.add(left);
        return this;
    }

    protected SqlBuilder bool(WhereParam param, BiConsumer<String, Object> consumer) {
        if (param.getValue() == null) return this;

        String field = param.getField();
        Expression column = this.getColumn(field);
        // name = true
        Expression left = new IsBooleanExpression()
                .withLeftExpression(column)
                .withIsTrue(Boolean.TRUE.equals(param.getValue()));

        this.wheres.add(left);
        return this;
    }

    protected SqlBuilder sort(String field, boolean asc) {
        Expression column = this.getColumn(field);
        OrderByElement orderByElement = new OrderByElement();
        orderByElement.setExpression(column);
        orderByElement.setAsc(asc);
        this.sorts.add(orderByElement);

        return this;
    }



    // inner help method
    Expression getColumn(String field) {
        Expression column = columnResolver.getProperty(field);
        if (column == null) {
            column = columnResolver.getProperty("`" + field + "`");
        }
        if (columnResolver.isAmbiguous() || column == null) {
            column = new net.sf.jsqlparser.schema.Column(EMPTY_TABLE, field);
        }

        return column;
    }
    JdbcParameter newJdbcParameter() {
        return new JdbcParameter(++paramIndex, false, "?");
    }
    Expression newOr(Expression left, Expression right) {
        if (left != null && right != null) {
            return new Parenthesis(new OrExpression(new Parenthesis(left), new Parenthesis(right)));
        }

        left = left != null ? left : right;
        if (left instanceof OrExpression) {
            left = new Parenthesis(left);
        }

        return left;
    }
    Expression newAnd(Expression left, Expression right) {
        if (left != null && right != null) {
            return new AndExpression(left, right);
        }

        return left != null ? left : right;
    }
    EqualsTo newEqToEmpty(Expression left) {
        return this.newEq(left, new StringValue(""));
    }
    EqualsTo newEq(Expression left, Expression right) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(left);
        equalsTo.setRightExpression(right);

        return equalsTo;
    }
    IsNullExpression newIsNull(Expression left) {
        IsNullExpression isNullExpression = new IsNullExpression();
        isNullExpression.setLeftExpression(left);

        return isNullExpression;
    }
    Between newBetween(Expression column, Expression start, Expression end) {
        Between between = new Between();
        between.setLeftExpression(column);
        between.setBetweenExpressionStart(start);
        between.setBetweenExpressionEnd(end);

        return between;
    }
    MinorThanEquals newLE(Expression left, Expression right) {
        MinorThanEquals minorThanEquals = new MinorThanEquals();
        minorThanEquals.setLeftExpression(left);
        minorThanEquals.setRightExpression(right);

        return minorThanEquals;
    }
    GreaterThanEquals newGE(Expression left, Expression right) {
        GreaterThanEquals greaterThanEquals = new GreaterThanEquals();
        greaterThanEquals.setLeftExpression(left);
        greaterThanEquals.setRightExpression(right);

        return greaterThanEquals;
    }
}

