package com.github.edgar615.sql;

public abstract class ClauseBuilder implements SqlBuilder {

  protected final WhereBuilder whereBuilder = WhereBuilder.create();

  public ClauseBuilder and(Predicate predicate) {
    whereBuilder.and(predicate);
    return this;
  }

  public ClauseBuilder or(Predicate predicate) {
    whereBuilder.or(predicate);
    return this;
  }

}
