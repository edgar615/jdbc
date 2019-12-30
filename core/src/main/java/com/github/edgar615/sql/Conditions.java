package com.github.edgar615.sql;

import com.google.common.base.Strings;

public class Conditions {

  public static final Condition NOT_NULL = o -> o != null;

  public static final Condition NOT_NULL_EMPTY = o -> o != null && Strings
      .isNullOrEmpty(o.toString());

  public static PredicateCondition notNull(Object value) {
    return new PredicateCondition(value, NOT_NULL);
  }

  public static PredicateCondition notNullOrEmpty(String value) {
    return new PredicateCondition(value, NOT_NULL_EMPTY);
  }
}
