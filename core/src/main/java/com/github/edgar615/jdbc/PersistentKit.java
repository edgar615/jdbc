package com.github.edgar615.jdbc;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PersistentKit<ID, T extends Persistent<ID>> {

  /**
   * 可以通过反射获取到所有的属性，但是因为实体类可以自动生成，所以这个方法也可以自动生成，不再通过反射. 如果要反射，类似的实现如下：
   * <pre>
   *   ReflectionUtils.getAllFields(this.getClass())
   *         .stream().map(f -> f.getName())
   *         .collect(Collectors.toList());
   * </pre>
   *
   * @return 属性集合
   */
  List<String> fields();

  /**
   * 可以通过反射获取到所有的属性，但是因为实体类可以自动生成，所以这个方法也可以自动生成，不再通过反射. 如果要反射，类似的实现如下：
   * <pre>
   *   ReflectionUtils
   *         .getAllFields(this.getClass(), Predicates.and(ReflectionUtils.withAnnotation(PrimaryKey.class)))
   *             .stream().map(f -> f.getName())
   *             .findFirst().get();
   * </pre>
   *
   * @return 主键属性
   */
  String primaryField();


  /**
   * 转换为map对象
   */
  void toMap(T entity, Map<String, Object> map);

  /**
   * 将map对象填充到实体中
   */
  void fromMap(Map<String, Object> map, T entity);


  /**
   * 虚拟列，MySQL5.7新增，新增修改是要忽略这个属性.
   * <p>
   * 可以通过反射获取到所有的属性，但是因为实体类可以自动生成，所以这个方法也可以自动生成，不再通过反射.
   */
  default List<String> virtualFields() {
    return Lists.newArrayList();
  }


  default void fillDefaultValue(T entity, Map<String, Object> defaultMap) {
    Map<String, Object> map = new HashMap<>();
    toMap(entity, map);
    Map<String, Object> newMap = new HashMap<>();
    defaultMap.forEach((k, v) -> {
      if (map.get(k) == null) {
        newMap.putIfAbsent(k, v);
      }
    });
    fromMap(newMap, entity);
  }
}
