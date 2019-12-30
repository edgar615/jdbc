package com.github.edgar615.sql;

import com.google.common.collect.Lists;

public class HavingBuilder implements SqlBuilder {

  private Predicate predicate;

  private HavingBuilder() {
  }

  private HavingBuilder(Predicate predicate) {
    this.predicate = predicate;
  }

  @Override
  public SQLBindings build() {
    if (predicate == null) {
      return SQLBindings.create("", Lists.newArrayList());
    }
    SQLBindings sqlBindings = predicate.toSql();
    StringBuilder sql = new StringBuilder(" having ")
        .append(sqlBindings.sql());
    return SQLBindings.create(sql.toString(), sqlBindings.bindings());
  }

  public static HavingBuilder create() {
    return new HavingBuilder();
  }

  public static HavingBuilder create(Predicate predicate) {
    return new HavingBuilder(predicate);
  }

  public HavingBuilder and(Predicate predicate) {
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

  public HavingBuilder or(Predicate predicate) {
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
