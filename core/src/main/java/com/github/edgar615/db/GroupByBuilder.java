package com.github.edgar615.db;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class GroupByBuilder implements SqlBuilder {

  private static final String GROUP_BY = " group by ";

  private final List<String> groupByList = new ArrayList<>();

  private GroupByBuilder() {
  }

  @Override
  public SQLBindings build() {
    if (groupByList.isEmpty()) {
      return SQLBindings.create("", Lists.newArrayList());
    }
    StringBuilder sql = new StringBuilder(GROUP_BY)
        .append(Joiner.on(",").join(groupByList));
    return SQLBindings.create(sql.toString(), Lists.newArrayList());
  }

  public static GroupByBuilder create() {
    return new GroupByBuilder();
  }

  public static GroupByBuilder create(String column) {
    GroupByBuilder groupByBuilder = new GroupByBuilder();
    groupByBuilder.add(column);
    return groupByBuilder;
  }

  public static GroupByBuilder create(List<String> groupByList) {
    GroupByBuilder groupByBuilder = new GroupByBuilder();
    for (String groupBy : groupByList) {
      groupByBuilder.add(groupBy);
    }
    return groupByBuilder;
  }

  public GroupByBuilder add(String column) {
    groupByList.add(column);
    return this;
  }
}
