package com.github.edgar615.sql;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;

public class SelectBuilder extends ClauseBuilder implements SqlBuilder {

  private final List<String> columns = new ArrayList<>();
  private final String table;
  private final GroupByBuilder groupByBuilder = GroupByBuilder.create();
  private final HavingBuilder havingBuilder = HavingBuilder.create();
  private final OrderByBuilder orderByBuilder = OrderByBuilder.create();
  private boolean distinct;
  private List<String> joins = new ArrayList<String>();
  private List<String> leftJoins = new ArrayList<String>();
  private int limit = 0;

  private int offset = 0;

  private boolean forUpdate;

  private boolean noWait;

  private SelectBuilder(String table) {
    this.table = table;
  }

  @Override
  public SelectBuilder and(Predicate predicate) {
    super.and(predicate);
    return this;
  }

  public SelectBuilder and(Predicate predicate, PredicateCondition condition) {
    if (condition.apply()) {
      and(predicate);
    }
    return this;
  }

  @Override
  public SelectBuilder or(Predicate predicate) {
    super.or(predicate);
    return this;
  }

  public SelectBuilder or(Predicate predicate, PredicateCondition condition) {
    if (condition.apply()) {
      or(predicate);
    }
    return this;
  }

  @Override
  public SQLBindings build() {
    StringBuilder sql = new StringBuilder("select ");
    List<Object> args = new ArrayList<>();

    if (distinct) {
      sql.append("distinct ");
    }

    if (columns.size() == 0) {
      sql.append("*");
    } else {
      sql.append(Joiner.on(", ").join(columns));
    }

    sql.append(" from ")
        .append(table);
    if (!joins.isEmpty()) {
      sql.append(" join ")
          .append(Joiner.on(" join ").join(joins));
    }
    if (!leftJoins.isEmpty()) {
      sql.append(" left join ")
          .append(Joiner.on(" left join ").join(leftJoins));
    }
    SQLBindings where = whereBuilder.build();
    sql.append(where.sql());
    args.addAll(where.bindings());

    SQLBindings groupBy = groupByBuilder.build();
    sql.append(groupBy.sql());
    args.addAll(groupBy.bindings());

    SQLBindings having = havingBuilder.build();
    sql.append(having.sql());
    args.addAll(having.bindings());

    SQLBindings orderBy = orderByBuilder.build();
    sql.append(orderBy.sql());
    args.addAll(orderBy.bindings());

    SQLBindings limitOffset = LimitBuilder.limitAndOffset(limit, offset).build();
    sql.append(limitOffset.sql());
    args.addAll(limitOffset.bindings());

    if (forUpdate) {
      sql.append(" for update");
      if (noWait) {
        sql.append(" nowait");
      }
    }
    return SQLBindings.create(sql.toString(), args);
  }

  public static SelectBuilder create(String table) {
    return new SelectBuilder(table);
  }

  public SelectBuilder column(String name) {
    columns.add(name);
    return this;
  }

  public SelectBuilder limit(int limit, int offset) {
    this.limit = limit;
    this.offset = offset;
    return this;
  }

  public SelectBuilder limit(int limit) {
    return limit(limit, 0);
  }

  public SelectBuilder distinct() {
    this.distinct = true;
    return this;
  }

  public SelectBuilder forUpdate() {
    forUpdate = true;
    return this;
  }

  public SelectBuilder forUpdateNoWait() {
    forUpdate = true;
    this.noWait = true;
    return this;
  }

  public SelectBuilder groupBy(String expr) {
    groupByBuilder.add(expr);
    return this;
  }

  public SelectBuilder having(Predicate predicate) {
    havingBuilder.and(predicate);
    return this;
  }

  public SelectBuilder asc(String name) {
    orderByBuilder.asc(name);
    return this;
  }

  public SelectBuilder desc(String name) {
    orderByBuilder.desc(name);
    return this;
  }

  public SelectBuilder join(String join) {
    joins.add(join);
    return this;
  }

  public SelectBuilder leftJoin(String join) {
    leftJoins.add(join);
    return this;
  }
}
