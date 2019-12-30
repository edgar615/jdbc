package com.github.edgar615.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class OrPredicate implements Predicate {

  private static final String joiner = " or ";
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

  public OrPredicate add(Predicate predicate) {
    this.predicates.add(predicate);
    return this;
  }
}
