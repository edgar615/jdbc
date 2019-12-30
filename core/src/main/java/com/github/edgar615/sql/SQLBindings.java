package com.github.edgar615.sql;

import java.util.Collections;
import java.util.List;

/**
 * SQL.
 *
 * @author Edgar
 */
public class SQLBindings {

  private final String sql;

  private final List<Object> bindings;

  private SQLBindings(String sql, List<Object> bindings) {
    this.sql = sql;
    //ImmutableList不能包含null，改为使用JDK默认的不可修改集合
    this.bindings = Collections.unmodifiableList(bindings);
  }

  public static SQLBindings create(String sql, List<Object> bindings) {
    return new SQLBindings(sql, bindings);
  }

  public String sql() {
    return sql;
  }

  public List<Object> bindings() {
    return bindings;
  }

}
