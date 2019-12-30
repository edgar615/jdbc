package com.github.edgar615.sql;

@FunctionalInterface
public interface Condition {

  boolean apply(Object obj);
}
