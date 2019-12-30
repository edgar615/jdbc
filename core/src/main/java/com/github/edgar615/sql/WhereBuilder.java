package com.github.edgar615.sql;

import com.google.common.collect.Lists;

public class WhereBuilder implements SqlBuilder {

  private Predicate predicate;

  private WhereBuilder() {
  }

  private WhereBuilder(Predicate predicate) {
    this.predicate = predicate;
  }

  @Override
  public SQLBindings build() {
    if (predicate == null) {
      return SQLBindings.create("", Lists.newArrayList());
    }
    SQLBindings sqlBindings = predicate.toSql();
    StringBuilder sql = new StringBuilder(" where ")
        .append(sqlBindings.sql());
    return SQLBindings.create(sql.toString(), sqlBindings.bindings());
  }

  public static WhereBuilder create() {
    return new WhereBuilder();
  }

  public static WhereBuilder create(Predicate predicate) {
    return new WhereBuilder(predicate);
  }

  public WhereBuilder and(Predicate predicate) {
    if (this.predicate == null) {
      this.predicate = predicate;
      return this;
    }
    if (this.predicate instanceof AndPredicate) {
      ((AndPredicate) this.predicate).add(predicate);
      return this;
    }
    this.predicate = Predicates.and(this.predicate, predicate);
    return this;
  }

  public WhereBuilder or(Predicate predicate) {
    if (predicate == null) {
      this.predicate = predicate;
      return this;
    }
    if (predicate instanceof OrPredicate) {
      ((AndPredicate) this.predicate).add(predicate);
      return this;
    }
    this.predicate = Predicates.or(this.predicate, predicate);
    return this;
  }
}
