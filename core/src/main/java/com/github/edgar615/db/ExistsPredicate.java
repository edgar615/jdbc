package com.github.edgar615.db;

import java.util.ArrayList;
import java.util.List;

public class ExistsPredicate implements Predicate {

    private final String table;
    private final List<Predicate> predicates = new ArrayList<>();

    public ExistsPredicate(String table) {
        this.table = table;
    }

    public ExistsPredicate and(Predicate predicate) {
        predicates.add(predicate);
        return this;
    }

    @Override
    public SQLBindings toSql() {
        StringBuilder stringBuilder = new StringBuilder("select 1 from ")
                .append(table);
        return null;
    }
}
