package com.github.edgar615.sql;

class PredicateCondition {
    private final Object obj;
    private final Condition condition;

    PredicateCondition(Object obj, Condition condition) {
      this.obj = obj;
      this.condition = condition;
    }

    public boolean apply() {
      return condition.apply(obj);
    }
  }
