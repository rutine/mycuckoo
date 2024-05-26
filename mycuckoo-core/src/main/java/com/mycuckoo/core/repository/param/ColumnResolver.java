package com.mycuckoo.core.repository.param;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:09
 */
public class ColumnResolver extends EnumerablePropertySource<Map<String, Expression>> {
    private boolean ambiguous = false;
    private String[] propertyNames;


    private ColumnResolver(Map<String, Expression> source) {
        super("columnSource", source);
    }

    @Override
    public String[] getPropertyNames() {
        return propertyNames;
    }

    @Nullable
    @Override
    public Expression getProperty(String name) {
        return this.source.get(name);
    }

    public boolean isAmbiguous() {
        return ambiguous;
    }



    public static ColumnResolver from(@Nonnull List<SelectItem<?>> items) {
        Map<String, Expression> source = new HashMap<>();
        boolean ambiguous = false;
        for (SelectItem item : items) {
            if (item.getExpression() instanceof net.sf.jsqlparser.schema.Column) {
                net.sf.jsqlparser.schema.Column column = (net.sf.jsqlparser.schema.Column) item.getExpression();
                if (item.getAlias() != null) {
                    source.put(item.getAlias().getName(), column);
                }
                else {
                    source.put(column.getColumnName(), column);
                }
            }
            else if (item.getExpression() instanceof AllTableColumns) {
                ambiguous = true;
            }
            else if (item.getExpression() instanceof AllColumns) {
                ambiguous = true;
            }
        }

        ColumnResolver expressionSource = new ColumnResolver(source);
        expressionSource.ambiguous = ambiguous;
        expressionSource.propertyNames = StringUtils.toStringArray(source.keySet());

        return expressionSource;
    }
}

