package com.github.edgar615.db;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class OrderByBuilder implements ISqlBuilder {

    private static final String ORDER_BY = " order by ";

    private final List<String> orderByList = new ArrayList<>();

    public OrderByBuilder asc(String column) {
        orderByList.add(String.format("%s asc", column));
        return this;
    }

    public OrderByBuilder desc(String column) {
        orderByList.add(String.format("%s desc", column));
        return this;
    }

    @Override
    public SQLBindings build() {
        if (orderByList.isEmpty()) {
            return SQLBindings.create("", Lists.newArrayList());
        }
        StringBuilder sql = new StringBuilder(ORDER_BY)
                .append(Joiner.on(",").join(orderByList));
        return SQLBindings.create(sql.toString(), Lists.newArrayList());
    }
}
