/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.edgar615.db;

import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.search.Criterion;
import com.github.edgar615.util.search.Example;
import com.github.edgar615.util.search.Op;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2017/5/18.
 *
 * @author Edgar  Date 2017/5/18
 */
public class SqlBuilder {

  private static final String REVERSE_KEY = "-";

  /**
   * 根据主键查询.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings findById(Class<? extends Persistent<ID>> elementType, ID id) {
    return findById(elementType, id, Lists.newArrayList());
  }

  /**
   * 根据主键查询.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param fields 返回的属性列表
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings findById(Class<? extends Persistent<ID>> elementType,
      ID id, List<String> fields) {
    Example example = primaryKeyExample(elementType, id);
    if (!fields.isEmpty()) {
      example.addFields(fields);
    }
    return findByExample(elementType, example);
  }

  /**
   * 根据条件查询.
   *
   * @param elementType 持久化对象 * @param example 返回的属性列表
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings findByExample(Class<? extends Persistent<ID>> elementType,
      Example example) {
    example = removeUndefinedField(elementType, example);
    Persistent<ID> domain = Persistent.create(elementType);
    String selectedField = selectFields(domain, example.fields());
    StringBuilder sql = new StringBuilder();
    sql.append("select ");
    if (example.isDistinct()) {
      sql.append("distinct ");
    }
    sql.append(selectedField)
        .append(" from ")
        .append(underscoreName(elementType.getSimpleName()));
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    if (!Strings.isNullOrEmpty(sqlBindings.sql())) {
      sql.append(" where ").append(sqlBindings.sql());
    }
    if (!example.orderBy().isEmpty()) {
      sql.append(SqlBuilder.orderSql(example));
    }
    return SQLBindings.create(sql.toString(), sqlBindings.bindings());
  }

  /**
   * 根据条件查询.
   *
   * @param elementType 持久化对象 * @param example 返回的属性列表
   * @param offset offset
   * @param limit limit
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings findByExample(Class<? extends Persistent<ID>> elementType,
      Example example, long offset, long limit) {
    SQLBindings sqlBindings = findByExample(elementType, example);
    StringBuilder sql = new StringBuilder(sqlBindings.sql());
    sql.append(" limit ?, ?");
    List<Object> args = new ArrayList<>(sqlBindings.bindings());
    args.add(offset);
    args.add(limit);
    return SQLBindings.create(sql.toString(), args);
  }

  /**
   * 根据条件查询总数.
   *
   * @param elementType 持久化对象 * @param example 返回的属性列表
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings countByExample(Class<? extends Persistent<ID>> elementType,
      Example example) {
    example = removeUndefinedField(elementType, example);
    StringBuilder sql = new StringBuilder();
    sql.append("select ");
    if (example.isDistinct()) {
      Persistent<ID> domain = Persistent.create(elementType);
      String selectedField = selectFields(domain, example.fields());
      sql.append("count(distinct(").append(selectedField).append("))");
    } else {
      sql.append("count(*)");
    }
    sql.append(" from ")
        .append(underscoreName(elementType.getSimpleName()));
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    if (!Strings.isNullOrEmpty(sqlBindings.sql())) {
      sql.append(" where ").append(sqlBindings.sql());
    }
    return SQLBindings.create(sql.toString(), sqlBindings.bindings());
  }

  /**
   * 根据主键删除.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings deleteById(Class<? extends Persistent<ID>> elementType, ID id) {
    Example example = primaryKeyExample(elementType, id);
    return deleteByExample(elementType, example);
  }

  /**
   * 根据条件删除.
   *
   * @param elementType 持久化对象
   * @param example 条件
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings deleteByExample(Class<? extends Persistent<ID>> elementType,
      Example example) {
    example = removeUndefinedField(elementType, example);
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    String tableName = StringUtils.underscoreName(elementType.getSimpleName());
    StringBuilder sql = new StringBuilder("delete from ")
        .append(tableName);
    if (!example.criteria().isEmpty()) {
      sql.append(" where ")
          .append(sqlBindings.sql());
    }

    return SQLBindings.create(sql.toString(), sqlBindings.bindings());
  }

  /**
   * 根据主键更新,忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, ID id) {
    Example example = Example.create()
        .equalsTo(createKit(persistent.getClass()).primaryField(), id);
    return updateByExample(persistent, addOrSub, nullFields, example);
  }

  /**
   * 根据条件更新,忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param example 条件
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, Example example) {
    PersistentKit kit = createKit(persistent.getClass());
    Map<String, Object> map = new TreeMap<>();
    kit.toMap(persistent, map);
    boolean noUpdated = map.values().stream()
        .allMatch(v -> v == null);
    boolean noAddOrSub = addOrSub == null
        || addOrSub.keySet().stream().allMatch(v -> !kit.fields().contains(v));
    boolean noNull = nullFields == null
        || nullFields.stream().allMatch(v -> !kit.fields().contains(v));
    if (noUpdated && noAddOrSub && noNull) {
      return null;
    }

    //对example做一次清洗，将表中不存在的条件删除，避免频繁出现500错误
    example = example.removeUndefinedField(kit.fields());
    List<String> columns = new ArrayList<>();
    List<Object> params = new ArrayList<>();
    //忽略虚拟列
    List<String> virtualFields = kit.virtualFields();
    map.forEach((k, v) -> {
      if (v != null && !virtualFields.contains(k)) {
        columns.add(StringUtils.underscoreName(k) + " = ?");
        params.add(v);
      }
    });
    if (addOrSub != null) {
      for (Map.Entry<String, Number> entry : addOrSub.entrySet()) {
        String key = entry.getKey();
        if (kit.fields().contains(key)) {
          String underscoreKey = StringUtils.underscoreName(key);
          BigDecimal value = new BigDecimal(entry.getValue().toString());
          if (value.compareTo(new BigDecimal(0)) > 0) {
            columns.add(
                underscoreKey + " = " + underscoreKey + " + " + value);
          } else {
            //int 以前这样取反的~(entry.getValue() - 1)
            columns.add(
                underscoreKey + " = " + underscoreKey + " - " + value.negate());
          }
        }
      }
    }
    if (nullFields != null) {
      List<String> nullColumns = nullFields.stream()
          .filter(f -> kit.fields().contains(f))
          .map(f -> StringUtils.underscoreName(f))
          .map(f -> f + " = null")
          .collect(Collectors.toList());
      columns.addAll(nullColumns);
    }

    if (columns.isEmpty()) {
      return null;
    }

    String tableName = StringUtils.underscoreName(persistent.getClass().getSimpleName());
    StringBuilder sql = new StringBuilder();
    sql.append("update ")
        .append(tableName)
        .append(" set ")
        .append(Joiner.on(",").join(columns));
    List<Object> args = new ArrayList<>(params);
    if (!example.criteria().isEmpty()) {
      SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
      sql.append(" where ")
          .append(sqlBindings.sql());
      args.addAll(sqlBindings.bindings());
    }
    return SQLBindings.create(sql.toString(), args);
  }

  /**
   * insert，忽略为null的属性.
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */

  public static <ID> SQLBindings insert(Persistent<ID> persistent) {
    PersistentKit kit = createKit(persistent.getClass());
    Map<String, Object> map = new TreeMap<>();
    kit.toMap(persistent, map);
    List<String> columns = new ArrayList<>();
    List<String> prepare = new ArrayList<>();
    List<Object> params = new ArrayList<>();
    List<String> virtualFields = kit.virtualFields();
    map.forEach((k, v) -> {
      if (v != null && !virtualFields.contains(k)) {
        columns.add(underscoreName(k));
        prepare.add("?");
        params.add(v);
      }
    });

    String tableName = underscoreName(persistent.getClass().getSimpleName());
    StringBuilder s = new StringBuilder();
    s.append("insert into ")
        .append(tableName)
        .append("(")
        .append(Joiner.on(",").join(columns))
        .append(") values(")
        .append(Joiner.on(",").join(prepare))
        .append(")");
    return SQLBindings.create(s.toString(), params);
  }

  /**
   * 返回完整的INSERT SQL，包括所有的字段
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings fullInsertSql(Persistent<ID> persistent) {
    PersistentKit kit = createKit(persistent.getClass());
    Map<String, Object> map = new HashMap<>();
    kit.toMap(persistent, map);
    List<String> virtualFields = kit.virtualFields();
    List<String> prepare = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    List<Object> params = new ArrayList<>();
    kit.fields()
        .stream().filter(f -> !virtualFields.contains(f))
        .forEach(f -> {
          columns.add(underscoreName(f.toString()));
          prepare.add("?");
          params.add(map.get(f.toString()));
        });

    String tableName = underscoreName(persistent.getClass().getSimpleName());
    StringBuilder s = new StringBuilder();
    s.append("insert into ")
        .append(tableName)
        .append("(")
        .append(Joiner.on(",").join(columns))
        .append(") values(")
        .append(Joiner.on(",").join(prepare))
        .append(")");
    return SQLBindings.create(s.toString(), params);
  }


  private static <ID> String selectFields(Persistent<ID> persistent, List<String> fields) {
    String selectedField;
    PersistentKit kit = createKit(persistent.getClass());
    Map<String, Object> map = new HashMap<>();
    kit.toMap(persistent, map);
    List<String> domainFields = kit.fields();
    if (fields.isEmpty()) {
      selectedField = Joiner.on(", ")
          .join(domainFields.stream()
              .map(f -> underscoreName(f))
              .collect(Collectors.toList()));
    } else {
      selectedField = Joiner.on(", ")
          .join(fields.stream()
              .filter(f -> domainFields.contains(f))
              .map(f -> underscoreName(f))
              .collect(Collectors.toList()));
    }
    return selectedField;
  }

  private static <ID> Example primaryKeyExample(Class<? extends Persistent<ID>> elementType,
      ID id) {
    return Example.create().equalsTo(createKit(elementType).primaryField(), id);
  }


  private static String orderSql(Example example) {
    if (example.orderBy().isEmpty()) {
      return "";
    }
    return " order by " + example.orderSql();
  }

  private static SQLBindings whereSql(List<Criterion> criteria) {
    if (criteria.isEmpty()) {
      return SQLBindings.create("", new ArrayList<>());
    }
    List<String> sql = new ArrayList<>();
    List<Object> bindings = new ArrayList<>();
    criteria
        .forEach(c -> addSqlFromCriterion(sql, bindings, c));
    return SQLBindings.create(Joiner.on(" and ").join(sql), bindings);
  }

  private static <ID, T extends Persistent<ID>> List<String> removeUndefinedColumn(
      Class<T> elementType,
      List<String> fields) {
    List<String> domainColumns = createKit(elementType).fields();
    return fields.stream()
        .filter(f -> domainColumns.contains(f))
        .collect(Collectors.toList());
  }

  private static <ID, T extends Persistent<ID>> Example removeUndefinedField(Class<T> elementType,
      Example example) {
    //对example做一次清洗，将表中不存在的条件删除，避免频繁出现500错误
    example = example.removeUndefinedField(createKit(elementType).fields());
    return example;
  }

  private static void addSqlFromCriterion(List<String> sql, List<Object> bindings,
      Criterion criterion) {
    SQLBindings sqlBindings = criterion(criterion);
    sql.add(sqlBindings.sql());
    bindings.addAll(sqlBindings.bindings());
  }

  private static SQLBindings criterion(Criterion criterion) {
    StringBuilder sql = new StringBuilder();
    List<Object> bindings = new ArrayList<>();
    sql.append(underscoreName(criterion.field())).append(criterion.condition());
    if (criterion.op() == Op.SW) {
      sql.append("?");
      bindings.add(criterion.value() + "%");
    } else if (criterion.op() == Op.EW) {
      sql.append("?");
      bindings.add("%" + criterion.value());
    } else if (criterion.op() == Op.CN) {
      sql.append("?");
      bindings.add("%" + criterion.value() + "%");
    } else if (criterion.op() == Op.BETWEEN) {
      sql.append("? and ?");
      bindings.add(criterion.value());
      bindings.add(criterion.secondValue());
    } else if (criterion.op() == Op.IS_NULL) {
      // do nothing
    } else if (criterion.op() == Op.IS_NOT_NULL) {
      // do nothing
    } else if (criterion.op() == Op.IN) {
      List<Object> values = (List<Object>) criterion.value();
      List<String> strings = values.stream()
          .map(v -> "?")
          .collect(Collectors.toList());
      sql.append("("
          + Joiner.on(",").join(strings)
          + ")");
      bindings.addAll(values);
    } else if (criterion.op() == Op.NOT_IN) {
      List<Object> values = (List<Object>) criterion.value();
      List<String> strings = values.stream()
          .map(v -> "?")
          .collect(Collectors.toList());
      sql.append("(")
          .append(Joiner.on(",").join(strings)).append(")");
      bindings.addAll(values);
    } else {
      sql.append("?");
      bindings.add(criterion.value());
    }
    return SQLBindings.create(sql.toString(), bindings);
  }

  private static String underscoreName(String name) {
    return StringUtils.underscoreName(name);
  }

  private static PersistentKit createKit(Class<? extends Persistent> clazz) {
    try {
      return (PersistentKit) Class.forName(clazz.getName() + "Kit").newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
