package com.github.edgar615.db;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class LimitBuilder implements ISqlBuilder {

    private static final String LIMIT = " limit ";

    private final int limit;

    private final int offset;

    public LimitBuilder(int limit) {
        this(limit, 0);
    }

    public LimitBuilder(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public SQLBindings build() {
        if (limit <= 0) {
            return SQLBindings.create("", Lists.newArrayList());
        }
        List<Object> args = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder(LIMIT)
                .append("?");
        args.add(limit);
        if (offset > 0) {
            stringBuilder.append(", ?");
            args.add(offset);
        }
        return SQLBindings.create(stringBuilder.toString(), args);
    }
}
