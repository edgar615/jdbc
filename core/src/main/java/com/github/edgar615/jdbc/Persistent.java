package com.github.edgar615.jdbc;

import java.io.Serializable;

/**
 * 实体类的接口.
 *
 * @param <ID> the type of the identifier
 */
public interface Persistent<ID> extends Serializable {

  /**
   * Returns the id of the entity.
   *
   * @return the id
   */
  ID id();

  static <ID> Persistent create(Class<? extends Persistent<ID>> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  void setId(ID id);

  /**
   * 设置自增主键
   */
  void setGeneratedKey(Number key);
}
