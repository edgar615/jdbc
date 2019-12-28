package com.github.edgar615.db;

public class WhereBuilder implements ISqlBuilder {

    private Predicate predicate;

    public WhereBuilder and(Predicate predicate) {
        if (this.predicate == null) {
            this.predicate = predicate;
        } else if (predicate instanceof AndPredicate){
            ((AndPredicate) predicate).add(predicate);
        } else if (predicate instanceof OrPredicate){
            ((OrPredicate) predicate).add(predicate);
        }
        return this;
    }

    @Override
    public SQLBindings build() {
        StringBuilder sql = new StringBuilder(" where ");
        return null;
    }
}
