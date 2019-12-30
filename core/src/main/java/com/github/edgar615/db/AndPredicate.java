package com.github.edgar615.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class AndPredicate implements Predicate {

  private static final String joiner = " and ";
  private final List<Predicate> predicates = new ArrayList<>();

  @Override
  public SQLBindings toSql() {
    if (predicates.isEmpty()) {
      return SQLBindings.create("", Lists.newArrayList());
    }
    if (predicates.size() == 1) {
      return predicates.get(0).toSql();
    }
    return Predicates.join(joiner, predicates);
  }

  public AndPredicate add(Predicate predicate) {
    this.predicates.add(predicate);
    return this;
  }
}
