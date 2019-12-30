package com.github.edgar615.db;

/**
 * 构造where的组件
 */
public interface Predicate {

  SQLBindings toSql();
}
