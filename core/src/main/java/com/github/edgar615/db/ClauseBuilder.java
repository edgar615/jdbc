package com.github.edgar615.db;

public class ClauseBuilder implements ISqlBuilder {

    private Predicate predicate;

    public ClauseBuilder and(Predicate predicate) {
        if (predicate == null) {

        }
        return this;
    }

    @Override
    public SQLBindings build() {
        StringBuilder sql = new StringBuilder(" where ");
        return null;
    }
}
