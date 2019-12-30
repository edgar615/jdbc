package com.github.edgar615.jdbc;

public class OptimisticLockException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String table;

  private final Object lockKey;

  private final Object lockValue;

  public OptimisticLockException(String table, Object lockKey, Object lockValue) {
    super("Could not update row in table " + table + " with lockKey:" + lockKey + "and lockValue:"
        + lockValue);
    this.table = table;
    this.lockKey = lockKey;
    this.lockValue = lockValue;
  }

  public String getTable() {
    return table;
  }

  public Object getLockKey() {
    return lockKey;
  }

  public Object getLockValue() {
    return lockValue;
  }
}
