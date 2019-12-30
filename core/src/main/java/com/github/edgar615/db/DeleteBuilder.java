package com.github.edgar615.db;

public class DeleteBuilder extends ClauseBuilder implements SqlBuilder {

    private final String table;

    private DeleteBuilder(String table) {
        this.table = table;
    }

    public static DeleteBuilder create(String table) {
        return new DeleteBuilder(table);
    }

    @Override
    public DeleteBuilder and(Predicate predicate) {
        super.and(predicate);
        return this;
    }

    @Override
    public DeleteBuilder or(Predicate predicate) {
        super.or(predicate);
        return this;
    }

    @Override
    public SQLBindings build() {
        SQLBindings where = whereBuilder.build();
        StringBuilder sql = new StringBuilder("delete from ")
            .append(table)
            .append(where.sql());
        return SQLBindings.create(sql.toString(), where.bindings());
    }
}
