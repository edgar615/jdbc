package com.github.edgar615.jdbc;

public class RowNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String table;

  private Object id;

  public RowNotFoundException() {
    super("found zero rows for query");
  }

  public RowNotFoundException(String table, Object id) {
    super("Could not find row in table " + table + " with id " + id);
    this.table = table;
    this.id = id;
  }

  public Object getId() {
    return id;
  }

  public String getTable() {
    return table;
  }

}
