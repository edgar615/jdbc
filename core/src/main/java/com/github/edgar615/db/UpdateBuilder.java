package com.github.edgar615.db;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateBuilder extends ClauseBuilder implements SqlBuilder {

  private final String table;

  private List<UpdatedColumn> columns = new ArrayList<>();

  private UpdateBuilder(String table) {
    this.table = table;
  }

  @Override
  public UpdateBuilder and(Predicate predicate) {
    super.and(predicate);
    return this;
  }

  @Override
  public UpdateBuilder or(Predicate predicate) {
    super.or(predicate);
    return this;
  }

  @Override
  public SQLBindings build() {
    List<String> fields = columns.stream()
        .map(c -> {
          if (c.getType() == UpdatedType.PARAMETER) {
            return c.getColumn() + " = ?";
          }
          return c.getColumn() + " = " + c.getValue();
        })
        .collect(Collectors.toList());

    List<Object> args = columns.stream()
        .filter(c -> c.getType() == UpdatedType.PARAMETER)
        .map(c -> c.getValue())
        .collect(Collectors.toList());

    SQLBindings where = whereBuilder.build();
    StringBuilder sql = new StringBuilder("update ")
        .append(table)
        .append(" set ")
        .append(Joiner.on(",").join(fields))
        .append(where.sql());
    args.addAll(where.bindings());
    return SQLBindings.create(sql.toString(), args);
  }

  public static UpdateBuilder create(String table) {
    return new UpdateBuilder(table);
  }

  private enum UpdatedType {
    PARAMETER,
    EXPR
  }

  private static class UpdatedColumn {

    private final String column;

    private final UpdatedType type;

    private final Object value;

    private UpdatedColumn(String column, UpdatedType type,
        Object value) {
      this.column = column;
      this.type = type;
      this.value = value;
    }

    public static UpdatedColumn param(String column, Object value) {
      return new UpdatedColumn(column, UpdatedType.PARAMETER, value);
    }

    public static UpdatedColumn expr(String column, String expr) {
      return new UpdatedColumn(column, UpdatedType.EXPR, expr);
    }

    public String getColumn() {
      return column;
    }

    public UpdatedType getType() {
      return type;
    }

    public Object getValue() {
      return value;
    }
  }

  public UpdateBuilder set(String column, Object value) {
    columns.add(UpdatedColumn.param(column, value));
    return this;
  }

  public UpdateBuilder setExpr(String column, String expr) {
    columns.add(UpdatedColumn.expr(column, expr));
    return this;
  }
}
