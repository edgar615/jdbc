package com.github.edgar615.dao;

import com.github.edgar615.jdbc.Jdbc;
import com.github.edgar615.jdbc.Persistent;
import com.github.edgar615.util.search.Example;
import java.util.List;
import java.util.Map;

/**
 * 基于JDBC的DAO实现
 *
 * @author Edgar
 */
public class BaseDaoImpl<ID, T extends Persistent<ID>> implements BaseDao<ID, T> {

  protected final Class<T> elementType;
  protected final Jdbc jdbc;

  public BaseDaoImpl(Class<T> elementType, Jdbc jdbc) {
    this.elementType = elementType;
    this.jdbc = jdbc;
  }

  @Override
  public void insert(T persistent) {
    jdbc.insert(persistent);
  }

  @Override
  public ID insertAndGeneratedKey(T persistent) {
    return jdbc.insertAndGeneratedKey(persistent);
  }

  @Override
  public void batchInsert(List<T> persistentList) {
    jdbc.batchInsert(persistentList);
  }

  @Override
  public void batchInsert(List<T> persistentList, int batchSize) {
    jdbc.batchInsert(persistentList, batchSize);
  }

  @Override
  public int deleteById(ID id) {
    return jdbc.deleteById(elementType, id);
  }

  @Override
  public int deleteByIdList(List<ID> idList) {
    int rows = 0;
    for (ID id : idList) {
      rows += deleteById(id);
    }
    return rows;
  }

  @Override
  public int deleteByExample(Example example) {
    return jdbc.deleteByExample(elementType, example);
  }

  @Override
  public int updateById(T persistent, Map<String, Number> addOrSub, List<String> nullFields,
      ID id) {
    return jdbc.updateById(persistent, addOrSub, nullFields, id);
  }

  @Override
  public int updateById(T persistent, Map<String, Number> addOrSub, List<String> nullFields,
      List<ID> idList) {
    int rows = 0;
    for (ID id : idList) {
      rows += updateById(persistent, addOrSub, nullFields, id);
    }
    return rows;
  }

  @Override
  public int updateByExample(T persistent, Map<String, Number> addOrSub, List<String> nullFields,
      Example example) {
    return jdbc.updateByExample(persistent, addOrSub, nullFields, example);
  }

  @Override
  public T findById(ID id, List<String> fields) {
    return jdbc.findById(elementType, id, fields);
  }

  @Override
  public List<T> findByExample(Example example) {
    return jdbc.findByExample(elementType, example);
  }

  @Override
  public List<T> findByExample(Example example, int start, int limit) {
    return jdbc.findByExample(elementType, example, start, limit);
  }

  @Override
  public int countByExample(Example example) {
    return jdbc.countByExample(elementType, example);
  }


}
