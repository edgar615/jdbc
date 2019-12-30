package com.github.edgar615.sql;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class InsertBuilder implements SqlBuilder {

  private final String table;

  private List<Entry<String, Object>> columns = new ArrayList<>();

  private InsertBuilder(String table) {
    this.table = table;
  }

  @Override
  public SQLBindings build() {
    List<String> prepare = columns.stream()
        .map(c -> "?")
        .collect(Collectors.toList());

    List<String> fields = columns.stream()
        .map(c -> c.getKey())
        .collect(Collectors.toList());

    List<Object> values = columns.stream()
        .map(c -> c.getValue())
        .collect(Collectors.toList());

    StringBuilder sql = new StringBuilder("insert into ").append(table).append("(")
        .append(Joiner.on(",").join(fields))
        .append(") values(")
        .append(Joiner.on(",").join(prepare))
        .append(")");
    return SQLBindings.create(sql.toString(), values);
  }

  public static InsertBuilder create(String table) {
    return new InsertBuilder(table);
  }

  public InsertBuilder set(String column, Object value) {
    columns.add(Maps.immutableEntry(column, value));
    return this;
  }

}
