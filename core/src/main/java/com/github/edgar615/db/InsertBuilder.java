package com.github.edgar615.db;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InsertBuilder implements ISqlBuilder {

    private static final long serialVersionUID = 1;

    private final String table;

    private List<String> columns = new ArrayList<>();

    private List<Object> values = new ArrayList<>();

    public InsertBuilder(String table) {
        this.table = table;
    }

    public InsertBuilder add(String column, Object value) {
        columns.add(column);
        values.add(value);
        return this;
    }

    @Override
    public SQLBindings build() {
        List<String> prepare = columns.stream()
                .map(c -> "?")
                .collect(Collectors.toList());

        StringBuilder sql = new StringBuilder("insert into ").append(table).append("(")
                .append(Joiner.on(",").join(columns))
                .append(") values(")
                .append(Joiner.on(",").join(prepare))
                .append(")");
        return SQLBindings.create(sql.toString(), values);
    }
}