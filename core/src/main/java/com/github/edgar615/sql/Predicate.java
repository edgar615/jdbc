package com.github.edgar615.sql;

/**
 * 构造where的组件
 */
public interface Predicate {

  SQLBindings toSql();
}
