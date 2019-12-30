package com.github.edgar615.jdbc;

import com.github.edgar615.sql.ClauseBuilder;
import com.github.edgar615.sql.DeleteBuilder;
import com.github.edgar615.sql.InsertBuilder;
import com.github.edgar615.sql.LimitBuilder;
import com.github.edgar615.sql.Predicate;
import com.github.edgar615.sql.Predicates;
import com.github.edgar615.sql.SQLBindings;
import com.github.edgar615.sql.SelectBuilder;
import com.github.edgar615.sql.UpdateBuilder;
import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.search.Criterion;
import com.github.edgar615.util.search.Example;
import com.github.edgar615.util.search.Op;
import com.google.common.base.Joiner;
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
public class JdbcUtils {

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
   * @param elementType 持久化对象
   * @param example 返回的属性列表
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings findByExample(Class<? extends Persistent<ID>> elementType,
      Example example) {
    return findByExample(elementType, example, false);
  }

  /**
   * 根据条件查询.
   *
   * @param elementType 持久化对象 * @param example 返回的属性列表
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings findByExample(Class<? extends Persistent<ID>> elementType,
      Example example, boolean onlyPrimaryKey) {
    example = removeUndefinedField(elementType, example);
    Persistent<ID> domain = Persistent.create(elementType);
    String selectedField;
    if (onlyPrimaryKey) {
      PersistentKit kit = createKit(elementType);
      selectedField = kit.primaryField();
    } else {
      selectedField = selectFields(domain, example.fields());
    }
    SelectBuilder selectBuilder = SelectBuilder.create(underscoreName(elementType.getSimpleName()))
        .column(selectedField);
    if (example.isDistinct()) {
      selectBuilder.distinct();
    }
    appendWhere(selectBuilder, example.criteria());

    example.orderBy().stream()
        .distinct()
        .forEach(o -> {
          if (o.startsWith(REVERSE_KEY)) {
            selectBuilder.desc(StringUtils.underscoreName(o.substring(1)));
          } else {
            selectBuilder.asc(StringUtils.underscoreName(o));
          }
        });
    return selectBuilder.build();
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
    return findByExample(elementType, example, false, offset, limit);
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
      Example example, boolean onlyPrimaryKey, long offset, long limit) {
    SQLBindings sqlBindings = findByExample(elementType, example, onlyPrimaryKey);
    StringBuilder sql = new StringBuilder(sqlBindings.sql());
    SQLBindings limitSql = LimitBuilder.limitAndOffset(limit, offset).build();
    sql.append(limitSql.sql());
    List<Object> args = new ArrayList<>(sqlBindings.bindings());
    args.addAll(limitSql.bindings());
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
    String countExpr;
    if (example.isDistinct()) {
      Persistent<ID> domain = Persistent.create(elementType);
      String selectedField = selectFields(domain, example.fields());
      countExpr = new StringBuilder("count(distinct(").append(selectedField).append("))")
          .toString();
    } else {
      countExpr = "count(*)";
    }
    SelectBuilder selectBuilder = SelectBuilder.create(underscoreName(elementType.getSimpleName()))
        .column(countExpr);
    appendWhere(selectBuilder, example.criteria());

    return selectBuilder.build();
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
    String tableName = StringUtils.underscoreName(elementType.getSimpleName());
    DeleteBuilder deleteBuilder = DeleteBuilder.create(tableName);
    appendWhere(deleteBuilder, example.criteria());
    return deleteBuilder.build();
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

    String tableName = StringUtils.underscoreName(persistent.getClass().getSimpleName());
    UpdateBuilder updateBuilder = UpdateBuilder.create(tableName);
    //忽略虚拟列
    List<String> virtualFields = kit.virtualFields();

    map.forEach((k, v) -> {
      if (v != null && !virtualFields.contains(k)) {
        updateBuilder.set(StringUtils.underscoreName(k), v);
      }
    });

    if (addOrSub != null) {
      for (Map.Entry<String, Number> entry : addOrSub.entrySet()) {
        String key = entry.getKey();
        if (kit.fields().contains(key)) {
          String underscoreKey = StringUtils.underscoreName(key);
          BigDecimal value = new BigDecimal(entry.getValue().toString());
          if (value.compareTo(new BigDecimal(0)) > 0) {
            updateBuilder.setExpr(underscoreKey, underscoreKey + " + " + value);
          } else {
            //int 以前这样取反的~(entry.getValue() - 1)
            updateBuilder.setExpr(underscoreKey, underscoreKey + " - " + value.negate());
          }
        }
      }
    }
    if (nullFields != null) {
      nullFields.stream()
          .filter(f -> kit.fields().contains(f))
          .forEach(f -> updateBuilder.setExpr(StringUtils.underscoreName(f), "null"));
    }
    appendWhere(updateBuilder, example.criteria());
    return updateBuilder.build();
  }

  /**
   * insert，忽略为null的属性.
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */

  public static <ID> SQLBindings insert(Persistent<ID> persistent) {
    String tableName = underscoreName(persistent.getClass().getSimpleName());
    InsertBuilder insertBuilder = InsertBuilder.create(tableName);
    PersistentKit kit = createKit(persistent.getClass());
    Map<String, Object> map = new TreeMap<>();
    kit.toMap(persistent, map);
    List<String> virtualFields = kit.virtualFields();
    map.forEach((k, v) -> {
      if (v != null && !virtualFields.contains(k)) {
        insertBuilder.set(underscoreName(k), v);
      }
    });
    return insertBuilder.build();
  }

  /**
   * 返回完整的INSERT SQL，包括所有的字段
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   * @return {@link SQLBindings}
   */
  public static <ID> SQLBindings fullInsertSql(Persistent<ID> persistent) {
    String tableName = underscoreName(persistent.getClass().getSimpleName());
    InsertBuilder insertBuilder = InsertBuilder.create(tableName);
    PersistentKit kit = createKit(persistent.getClass());
    Map<String, Object> map = new HashMap<>();
    kit.toMap(persistent, map);
    List<String> virtualFields = kit.virtualFields();
    List<String> fields = kit.fields();
    fields.stream()
        .forEach(f -> {
          if (!virtualFields.contains(f)) {
            insertBuilder.set(underscoreName(f), map.get(f));
          }
        });
    return insertBuilder.build();
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

  private static void appendWhere(ClauseBuilder sqlBuilder, List<Criterion> criteria) {
    criteria.stream()
        .map(criterion -> criterion(criterion))
        .forEach(predicate -> sqlBuilder.and(predicate));
  }

  private static <ID, T extends Persistent<ID>> Example removeUndefinedField(Class<T> elementType,
      Example example) {
    //对example做一次清洗，将表中不存在的条件删除，避免频繁出现500错误
    example = example.removeUndefinedField(createKit(elementType).fields());
    return example;
  }

  private static Predicate criterion(Criterion criterion) {
    String field = StringUtils.underscoreName(criterion.field());
    if (criterion.op() == Op.SW) {
      return Predicates.startWith(field, criterion.value().toString());
    } else if (criterion.op() == Op.EW) {
      return Predicates.endWith(field, criterion.value().toString());
    } else if (criterion.op() == Op.CN) {
      return Predicates.contains(field, criterion.value().toString());
    } else if (criterion.op() == Op.BETWEEN) {
      return Predicates.between(field, criterion.value(), criterion.secondValue());
    } else if (criterion.op() == Op.IS_NULL) {
      return Predicates.isNull(field);
    } else if (criterion.op() == Op.IS_NOT_NULL) {
      return Predicates.isNotNull(field);
    } else if (criterion.op() == Op.IN) {
      return Predicates.in(field, (List<Object>) criterion.value());
    } else if (criterion.op() == Op.NOT_IN) {
      return Predicates.notIn(field, (List<Object>) criterion.value());
    } else if (criterion.op() == Op.EQ) {
      return Predicates.equalsTo(field, criterion.value());
    } else if (criterion.op() == Op.NE) {
      return Predicates.notEqualsTo(field, criterion.value());
    } else if (criterion.op() == Op.GT) {
      return Predicates.greaterThan(field, criterion.value());
    } else if (criterion.op() == Op.GE) {
      return Predicates.greaterThanOrEqualTo(field, criterion.value());
    } else if (criterion.op() == Op.LT) {
      return Predicates.lessThan(field, criterion.value());
    } else if (criterion.op() == Op.LE) {
      return Predicates.lessThanOrEqualTo(field, criterion.value());
    } else if (criterion.op() == Op.REGEXP) {
      return Predicates.regexp(field, criterion.value().toString());
    }
    throw new UnsupportedOperationException(criterion.op().name());
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
