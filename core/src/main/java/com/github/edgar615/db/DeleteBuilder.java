package com.github.edgar615.db;

public class DeleteBuilder implements ISqlBuilder {

    private final String table;

    public DeleteBuilder(String table) {
        this.table = table;
    }

    @Override
    public SQLBindings build() {
        StringBuilder sql = new StringBuilder("delete from ")
                .append(table);
        return null;
    }
}
