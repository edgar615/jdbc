package com.github.edgar615.sql;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class LimitBuilder implements SqlBuilder {

  private static final String LIMIT = " limit ";

  private final long limit;

  private final long offset;

  private LimitBuilder(long limit, long offset) {
    this.limit = limit;
    this.offset = offset;
  }

  @Override
  public SQLBindings build() {
    if (limit <= 0) {
      return SQLBindings.create("", Lists.newArrayList());
    }
    StringBuilder stringBuilder = new StringBuilder(LIMIT);
    List<Object> args = new ArrayList<>();
    if (offset <= 0) {
      stringBuilder.append("?");
      args.add(limit);
    } else {
      stringBuilder.append("?, ?");
      args.add(offset);
      args.add(limit);
    }
    return SQLBindings.create(stringBuilder.toString(), args);
  }

  public static LimitBuilder limit(long limit) {
    return new LimitBuilder(limit, 0);
  }

  public static LimitBuilder limitAndOffset(long limit, long offset) {
    return new LimitBuilder(limit, offset);
  }
}
