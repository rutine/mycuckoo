package com.mycuckoo.core.repository.auth;

import com.mycuckoo.core.repository.annotation.PreAuth;
import com.mycuckoo.core.repository.param.PreAuthInfo;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:30
 */
public class RowResolver extends EnumerablePropertySource<Map<String, PreAuthInfo>> {
    private static String rowSql = "SELECT u_1.user_id FROM uum_user u_1 WHERE u_1.org_id = ?";

    private static Select select = parse(rowSql);

    private String[] propertyNames;
    private PreAuthInfo defaultAuth;


    public static RowResolver from(@NonNull PreAuthInfo authInfo) {
        return from(authInfo, null);
    }

    public static RowResolver from(@NonNull PreAuthInfo authInfo, PreAuthInfo authInfo2) {
        Map<String, PreAuthInfo> source = new HashMap<>();
        source.put(authInfo.getTable(), authInfo);
        if (authInfo2 != null) {
            source.put(authInfo2.getTable(), new PreAuthInfo(authInfo2.getTable(), authInfo2.getAlias(), authInfo2.getTenant(), authInfo2.getUser()));
        }

        return new RowResolver(source, StringUtils.toStringArray(source.keySet()), authInfo2);
    }


    private RowResolver(Map<String, PreAuthInfo> source, String[] propertyNames, PreAuthInfo defaultAuth) {
        super("rowSource", source);
        this.propertyNames = propertyNames;
        this.defaultAuth = defaultAuth;
    }

    @Override
    public String[] getPropertyNames() {
        return propertyNames;
    }

    @Nullable
    @Override
    public PreAuthInfo getProperty(String name) {
        return this.source.get(name);
    }

    public boolean isPreFiltered() {
        return this.defaultAuth != null;
    }

    public PreAuthInfo getDefaultAuth() {
        return this.defaultAuth;
    }

    public PlainSelect getSelect() {
        PreAuthInfo authData = this.getDefaultAuth();
        Table table = new Table(authData.getTable());
        Column column = new Column("id");
        PlainSelect select = new PlainSelect()
                .withSelectItems(Arrays.asList(new SelectItem<>(column)))
                .withFromItem(table);

        return select;
    }

    //where 行权限
    public Expression rowExpression(Table table, boolean onlyTenant, BiConsumer<String, Object> addParameters) {
        // column in (select * from table)
        PreAuthInfo preAuthInfo = this.getProperty(table.getName());
        EqualsTo left = null;
        if ((preAuthInfo.getRow() & PreAuth.Row.TENANT.getValue()) == PreAuth.Row.TENANT.getValue()) {
            RowInfo authInfo = RowContextHolder.get();
            left = new EqualsTo()
                    .withLeftExpression(new Column(table.getAlias() == null ? null : table, preAuthInfo.getTenant()))
                    .withRightExpression(new LongValue(authInfo.getOrgId()));
        }
        if (onlyTenant) {
            return left;
        }

        InExpression right = null;
        if ((preAuthInfo.getRow() & PreAuth.Row.USER.getValue()) == 2) {
            right = new InExpression()
                    .withLeftExpression(new Column(table.getAlias() == null ? null : table, preAuthInfo.getUser()))
                    .withRightExpression(new ParenthesedSelect().withSelect(parse(newSql())));
        }

        if (left != null && right != null) {
            return new AndExpression(left, right);
        }

        return left != null ? left : right;
    }

    //inner join 行权限
    public Collection<Join> rowJoins(Table table, BiConsumer<String, Object> addParameters) {
        PreAuthInfo preAuthInfo = this.getProperty(table.getName());
        if ((preAuthInfo.getRow() & PreAuth.Row.USER.getValue()) != 2) {
           return Collections.emptyList();
        }

        PlainSelect authSelect = (PlainSelect) parse(newSql());
        Table authTable = (Table) authSelect.getFromItem();

        Expression on = new EqualsTo()
                .withLeftExpression(authSelect.getSelectItem(0).getExpression())
                .withRightExpression(new Column(table.getAlias() == null ? null : table, preAuthInfo.getUser()));
        if ((preAuthInfo.getRow() & PreAuth.Row.TENANT.getValue()) == PreAuth.Row.TENANT.getValue()) {
            RowInfo authInfo = RowContextHolder.get();
            on = new AndExpression(on, new EqualsTo()
                    .withLeftExpression(new Column(table.getAlias() == null ? null : table, preAuthInfo.getTenant()))
                    .withRightExpression(new LongValue(authInfo.getOrgId())));
        }
        if (authSelect.getWhere() != null) {
            on = new AndExpression(on, authSelect.getWhere());
        }

        Join join = new Join()
                .withInner(true)
                .setFromItem(authTable)
                .addOnExpression(on);

        List<Join> joins = Arrays.asList(join);
        if (authSelect.getJoins() != null) {
            joins.addAll(authSelect.getJoins());
        }

        return joins;
    }

    private static String newSql() {
        RowInfo authInfo = RowContextHolder.get();

        Map<Integer, Long> params = new HashMap(2);
        params.put(0, authInfo.getOrgId());
        params.put(1, authInfo.getUserId());

        int index = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = rowSql.length(); i < len; i++) {
            char ch = rowSql.charAt(i);
            if (ch == '?') {
                builder.append(params.get(index++));
            } else {
                builder.append(ch);
            }
        }

        return builder.toString();
    }
    private static Select parse(String sql) {
        try {
            return ((Select) CCJSqlParserUtil.parse(sql));
        }  catch (JSQLParserException e) {
            // 无法解析的用一般方法返回count语句
            throw new RuntimeException("Init RowResolver Error");
        }
    }
}
