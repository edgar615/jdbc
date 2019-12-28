package com.github.edgar615.db;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class Predicates {

    public static Predicate allBitsSet(final String expr, final long bits) {
        return () -> SQLBindings.create(String.format("(%s & ?) = ?", expr), Lists.newArrayList(bits, bits));
    }

    public static Predicate anyBitsSet(final String expr, final long bits) {
        return () -> SQLBindings.create(String.format("(%s & ?) > 0", expr), Lists.newArrayList(bits));
    }

    public static Predicate equalsTo(final String expr, final Object value) {
        return () -> SQLBindings.create(String.format("%s = ?", expr), Lists.newArrayList(value));
    }

    public static Predicate equalsToExpr(final String expr1, final String expr2) {
        return () -> SQLBindings.create(String.format("%s = %s", expr1, expr2), Lists.newArrayList());
    }

    public static Predicate notEqualsTo(final String expr, final Object value) {
        return () -> SQLBindings.create(String.format("%s <> ?", expr), Lists.newArrayList(value));
    }

    public static Predicate notEqualsToExpr(final String expr1, final String expr2) {
        return () -> SQLBindings.create(String.format("%s <> %s", expr1, expr2), Lists.newArrayList());
    }

    public static Predicate isNull(final String expr) {
        return () -> SQLBindings.create(String.format("%s is null", expr), Lists.newArrayList());
    }

    public static Predicate isNotNull(final String expr) {
        return () -> SQLBindings.create(String.format("%s is not null", expr), Lists.newArrayList());
    }

    public static Predicate greaterThan(final String expr, final Object value) {
        return () -> SQLBindings.create(String.format("%s > ?", expr), Lists.newArrayList(value));
    }

    public static Predicate greaterThanExpr(final String expr1, final String expr2) {
        return () -> SQLBindings.create(String.format("%s > %s", expr1, expr2), Lists.newArrayList());
    }

    public static Predicate greaterThanOrEqualTo(final String expr, final Object value) {
        return () -> SQLBindings.create(String.format("%s >= ?", expr), Lists.newArrayList(value));
    }

    public static Predicate greaterThanOrEqualToExpr(final String expr1, final String expr2) {
        return () -> SQLBindings.create(String.format("%s >= %s", expr1, expr2), Lists.newArrayList());
    }

    public static Predicate lessThan(final String expr, final Object value) {
        return () -> SQLBindings.create(String.format("%s < ?", expr), Lists.newArrayList(value));
    }

    public static Predicate lessThanExpr(final String expr1, final String expr2) {
        return () -> SQLBindings.create(String.format("%s < %s", expr1, expr2), Lists.newArrayList());
    }

    public static Predicate lessThanOrEqualTo(final String expr, final Object value) {
        return () -> SQLBindings.create(String.format("%s <= ?", expr), Lists.newArrayList(value));
    }

    public static Predicate lessThanOrEqualToExpr(final String expr1, final String expr2) {
        return () -> SQLBindings.create(String.format("%s <= %s", expr1, expr2), Lists.newArrayList());
    }

    public static Predicate startWith(final String expr, final String value) {
        return () -> SQLBindings.create(String.format("%s like ?", expr), Lists.newArrayList(value +"%"));
    }

    public static Predicate endWith(final String expr, final String value) {
        return () -> SQLBindings.create(String.format("%s like ?", expr), Lists.newArrayList("%"+value));
    }

    public static Predicate contains(final String expr, final String value) {
        return () -> SQLBindings.create(String.format("%s like ?", expr), Lists.newArrayList("%"+value +"%"));
    }

    public static Predicate regexp(final String expr, final String value) {
        return () -> SQLBindings.create(String.format("%s regexp ?", expr), Lists.newArrayList(value));
    }

    public static Predicate in(final String expr, final List<Object> value) {
        List<String> strings = value.stream()
                .map(v -> "?")
                .collect(Collectors.toList());
        StringBuilder sql = new StringBuilder(expr)
                .append(" in ")
                .append("(")
                .append(Joiner.on(",").join(strings))
                .append(")");
        return () -> SQLBindings.create(sql.toString(), value);
    }

    public static Predicate inExpr(final String expr1, final String expr2) {
        StringBuilder sql = new StringBuilder(expr1)
                .append(" in ")
                .append("(")
                .append(expr2)
                .append(")");
        return () -> SQLBindings.create(sql.toString(), Lists.newArrayList());
    }

    public static Predicate notIn(final String expr, final List<Object> value) {
        List<String> strings = value.stream()
                .map(v -> "?")
                .collect(Collectors.toList());
        StringBuilder sql = new StringBuilder(expr)
                .append(" not in ")
                .append("(")
                .append(Joiner.on(",").join(strings))
                .append(")");
        return () -> SQLBindings.create(sql.toString(), value);
    }

    public static Predicate notInExpr(final String expr1, final String expr2) {
        StringBuilder sql = new StringBuilder(expr1)
                .append(" not in ")
                .append("(")
                .append(expr2)
                .append(")");
        return () -> SQLBindings.create(sql.toString(), Lists.newArrayList());
    }

    public static Predicate existsExpr(final String expr) {
        StringBuilder sql = new StringBuilder(" exists ")
                .append("(")
                .append(expr)
                .append(")");
        return () -> SQLBindings.create(sql.toString(), Lists.newArrayList());
    }

    public static Predicate notExistsExpr(final String expr) {
        StringBuilder sql = new StringBuilder(" not exists ")
                .append("(")
                .append(expr)
                .append(")");
        return () -> SQLBindings.create(sql.toString(), Lists.newArrayList());
    }

    public static Predicate between(final String expr, final Object value1, final Object value2) {
        return () -> SQLBindings.create(String.format("%s between ? and ?", expr), Lists.newArrayList(value1, value2));
    }

    public static SQLBindings join(String joiner, List<Predicate> predicates) {
        List<SQLBindings> sqlBindings = predicates.stream()
                .map(predicate -> predicate.toSql())
                .collect(Collectors.toList());
        String sql = sqlBindings.stream()
                .map(sqlBinding -> sqlBinding.sql())
                .collect(Collectors.joining(joiner));
        List<Object> args = sqlBindings.stream()
                .flatMap(sqlBinding -> sqlBinding.bindings().stream())
                .collect(Collectors.toList());
        return SQLBindings.create(sql, args);
    }
}
