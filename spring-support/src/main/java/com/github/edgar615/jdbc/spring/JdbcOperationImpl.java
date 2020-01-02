package com.github.edgar615.jdbc.spring;

import com.github.edgar615.jdbc.JdbcUtils;
import com.github.edgar615.entity.Persistent;
import com.github.edgar615.entity.PersistentKit;
import com.github.edgar615.sql.SQLBindings;
import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.reflect.ReflectionException;
import com.github.edgar615.util.search.Example;
import com.google.common.collect.Lists;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Created by Edgar on 2017/8/8.
 *
 * @author Edgar  Date 2017/8/8
 */
public class JdbcOperationImpl implements JdbcOperation {

  private final DataSource dataSource;

  public JdbcOperationImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public <ID> void insert(Persistent<ID> persistent) {
    SQLBindings sqlBindings = JdbcUtils.insert(persistent);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> ID insertAndGeneratedKey(Persistent<ID> persistent) {
    if (persistent.id() != null) {
      insert(persistent);
      return persistent.id();
    }
    PersistentKit kit = null;
    try {
       kit = (PersistentKit) Class.forName(persistent.getClass().getName() + "Kit").newInstance();
    } catch (Exception e) {
      throw new ReflectionException("create instance failed");
    }
    SQLBindings sqlBindings = JdbcUtils.insert(persistent);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    PersistentKit finalKit = kit;
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps
              = connection.prepareStatement(sqlBindings.sql(),
              new String[]{StringUtils.underscoreName(
                  finalKit.primaryField())}
          );
          int i = 1;
          for (Object arg : sqlBindings.bindings()) {
            ps.setObject(i++, arg);
          }
          return ps;
        },
        keyHolder);
    persistent.setGeneratedKey(keyHolder.getKey());
    return persistent.id();
  }

  @Override
  public <ID, T extends Persistent<ID>> void batchInsert(List<T> persistentList) {
    if (persistentList == null || persistentList.isEmpty()) {
      return;
    }
    if (persistentList.size() == 1) {
      insert(persistentList.get(0));
      return;
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    List<SQLBindings> sqlBindingsList = persistentList.stream()
        .map(p -> JdbcUtils.fullInsertSql(p))
        .collect(Collectors.toList());
    String sql = sqlBindingsList.get(0).sql();
    List<Object[]> args = sqlBindingsList.stream()
        .map(sqlBindings -> sqlBindings.bindings().toArray())
        .collect(Collectors.toList());
    jdbcTemplate.batchUpdate(sql, args);
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id) {
    SQLBindings sqlBindings = JdbcUtils.deleteById(elementType, id);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType,
      Example example) {
    SQLBindings sqlBindings = JdbcUtils.deleteByExample(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> int updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, ID id) {
    SQLBindings sqlBindings = JdbcUtils.updateById(persistent, addOrSub, nullFields, id);
    if (sqlBindings == null) {
      return 0;
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  @Override
  public <ID> int updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields,
      Example example) {
    SQLBindings sqlBindings = JdbcUtils.updateByExample(persistent, addOrSub, nullFields, example);
    if (sqlBindings == null) {
      return 0;
    }
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.update(sqlBindings.sql(), sqlBindings.bindings().toArray());
  }

  /**
   * 根据主键更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  @Override
  public <ID> int updateById(Persistent<ID> persistent, ID id) {
    return updateById(persistent, new HashMap<>(), new ArrayList<>(), id);
  }

  /**
   * 根据条件更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param example 查询条件
   * @param <ID> 条件集合
   * @return 修改记录数
   */
  @Override
  public <ID> int updateByExample(Persistent<ID> persistent, Example example) {
    return updateByExample(persistent, new HashMap<>(), new ArrayList<>(), example);
  }

  @Override
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id) {
    return findById(elementType, id, Lists.newArrayList());
  }

  @Override
  public <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id,
      List<String> fields) {
    SQLBindings sqlBindings = JdbcUtils.findById(elementType, id, fields);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    List<T> result =
        jdbcTemplate.query(sqlBindings.sql(), sqlBindings.bindings().toArray(),
            BeanPropertyRowMapper.newInstance(elementType));
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType,
      Example example) {
    SQLBindings sqlBindings = JdbcUtils.findByExample(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sqlBindings.sql(), sqlBindings.bindings().toArray(),
        BeanPropertyRowMapper.newInstance(elementType));
  }

  @Override
  public <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example,
      int start, int limit) {
    SQLBindings sqlBindings = JdbcUtils.findByExample(elementType, example, start, limit);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sqlBindings.sql(), sqlBindings.bindings().toArray(),
        BeanPropertyRowMapper.newInstance(elementType));
  }

  @Override
  public <ID, T extends Persistent<ID>> int countByExample(Class<T> elementType,
      Example example) {
    SQLBindings sqlBindings = JdbcUtils.countByExample(elementType, example);
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate
        .queryForObject(sqlBindings.sql(), Integer.class, sqlBindings.bindings().toArray());
  }


}
