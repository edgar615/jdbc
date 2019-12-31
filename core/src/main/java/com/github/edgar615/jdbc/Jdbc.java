package com.github.edgar615.jdbc;

import com.github.edgar615.util.page.Page;
import com.github.edgar615.util.page.Pagination;
import com.github.edgar615.util.search.Example;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据访问层的接口.
 *
 * @author Edgar  Date 2017/5/22
 */
public interface Jdbc {

  /**
   * 新增数据.
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   */
  <ID> void insert(Persistent<ID> persistent);

  /**
   * insert一条数据，并返回自增主键
   *
   * @param persistent 持久化对象
   * @param <ID> 主键类型
   */
  <ID> ID insertAndGeneratedKey(Persistent<ID> persistent);

  /**
   * 批量插入，调用方应该控制批量插入的长度
   *
   * @param persistentList 持久化对象的集合
   * @param <ID> 主键列席
   * @param <T> 持久化对象
   */
  <ID, T extends Persistent<ID>> void batchInsert(List<T> persistentList);

  /**
   * 根据主键删除.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 删除记录数
   */
  <ID, T extends Persistent<ID>> int deleteById(Class<T> elementType, ID id);

  /**
   * 根据条件删除.
   *
   * @param elementType 持久化对象
   * @param example 查询条件
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 删除记录数
   */
  <ID, T extends Persistent<ID>> int deleteByExample(Class<T> elementType, Example example);

  /**
   * 根据主键更新，忽略实体中的null
   *
   * @param persistent 持久化对象
   * @param addOrSub 需要做增加或者减去的字段，value为正数表示增加，负数表示减少
   * @param nullFields 需要设为null的字段
   * @param id 主键ID
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  <ID> int updateById(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields,
      ID id);

  /**
   * 根据主键批量更新，忽略实体中的null
   *
   * @param persistent 持久化对象
   * @param addOrSub 需要做增加或者减去的字段，value为正数表示增加，负数表示减少
   * @param nullFields 需要设为null的字段
   * @param idList 主键ID的集合
   * @param <ID> 主键类型
   * @return 修改记录数
   */
//  <ID> int batchUpdateById(Persistent<ID> persistent,
//      Map<String, Number> addOrSub,
//      List<String> nullFields,
//      List<ID> idList);

  /**
   * 根据条件更新，忽略实体中的null
   *
   * @param persistent 持久化对象
   * @param addOrSub 需要做增加或者减去的字段，value为正数表示增加，负数表示减少
   * @param nullFields 需要设为null的字段
   * @param example 查询条件
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  <ID> int updateByExample(Persistent<ID> persistent,
      Map<String, Number> addOrSub,
      List<String> nullFields, Example example);

  /**
   * 根据主键查找.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param fields 返回的属性列表
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象
   */
  <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id, List<String> fields);

  /**
   * 根据条件查找.
   *
   * @param elementType 持久化对象,
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象列表
   */
  <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example);

  /**
   * 根据条件查找.
   *
   * @param elementType 持久化对象
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param start 开始索引
   * @param limit 查询数量
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象列表
   */
  <ID, T extends Persistent<ID>> List<T> findByExample(Class<T> elementType, Example example,
      int start,
      int limit);

  /**
   * 根据条件查找.
   *
   * @param elementType 持久化对象
   * @param example 查询条件
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 记录数
   */
  <ID, T extends Persistent<ID>> int countByExample(Class<T> elementType, Example example);

  /**
   * 批量插入，如果超过了batchSize，会拆分多次执行
   *
   * @param persistentList 持久化对象的集合
   * @param batchSize 每次处理的记录数量
   * @param <ID> 主键列席
   * @param <T> 持久化对象
   */
  default <ID, T extends Persistent<ID>> void batchInsert(List<T> persistentList, int batchSize) {
    int start = 0;
    while (start >= persistentList.size()) {
      int len = Math.min(batchSize, persistentList.size() - start);
      batchInsert(persistentList.subList(start, len));
      start += len;
    }
  }

  /**
   * 根据主键更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  default <ID> int updateById(Persistent<ID> persistent, ID id) {
    return updateById(persistent, new HashMap<>(), new ArrayList<>(), id);
  }

  /**
   * 根据主键批量更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param idList 主键列表
   * @param <ID> 主键类型
   * @return 修改记录数
   */
//  default <ID> int batchUpdateById(Persistent<ID> persistent, List<ID> idList) {
//    return batchUpdateById(persistent, new HashMap<>(), new ArrayList<>(), idList);
//  }

  /**
   * 根据主键列表，迭代调用updateById删除记录，用于简化部分业务代码。<b>这个方法不是用<code>id in (...)</code>或者batch来更新.</b>
   *
   * @param persistentList 持久化对象的列表
   * @param idList 主键列表
   * @param <ID> 主键类型
   * @return 修改记录数
   */
  default <ID> int updateById(List<Persistent<ID>> persistentList, List<ID> idList) {
    Preconditions.checkArgument(persistentList.size() == idList.size());
    int rows = 0;
    for (int i = 0; i < persistentList.size(); i++) {
      rows += updateById(persistentList.get(i), idList.get(i));
    }
    return rows;
  }

  /**
   * 根据条件更新，忽略实体中的null.
   *
   * @param persistent 持久化对象
   * @param example 查询条件
   * @param <ID> 条件集合
   * @return 修改记录数
   */
  default <ID> int updateByExample(Persistent<ID> persistent, Example example) {
    return updateByExample(persistent, new HashMap<>(), new ArrayList<>(), example);
  }

  /**
   * 分页查找.
   *
   * @param elementType 持久化对象
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param page 页码
   * @param pageSize 每页数量
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 分页对象
   */
  default <ID, T extends Persistent<ID>> Pagination<T> pagination(Class<T> elementType,
      Example example,
      int page, int pageSize) {
    Preconditions.checkArgument(page > 0, "page must greater than 0");
    Preconditions.checkArgument(pageSize > 0, "pageSize must greater than 0");
    //查询总数
    final int totalRecords = countByExample(elementType, example);
    if (totalRecords == 0) {
      return Pagination.newInstance(1, pageSize, totalRecords, Lists.newArrayList());
    }
    int pageCount = totalRecords / pageSize;
    if (totalRecords > pageSize * pageCount) {
      pageCount++;
    }
    if (pageCount < page) {
      page = pageCount;
    }
    int offset = (page - 1) * pageSize;
    List<T> records = findByExample(elementType, example, offset, pageSize);
    return Pagination.newInstance(page, pageSize, totalRecords, records);
  }

  /**
   * 根据条件查找，返回第一条数据，如果未找到任何数据，返回null；如果找到多条，返回第一条.
   *
   * @param elementType 持久化对象
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象，如果未找到任何数据，返回null
   */
  default <ID, T extends Persistent<ID>> T findFirstByExample(Class<T> elementType,
      Example example) {
    List<T> result = findByExample(elementType, example, 0, 1);
    if (result == null || result.isEmpty()) {
      return null;
    }
    return result.get(0);
  }

  /**
   * 根据条件查找一条数据，如果未找到任何数据，返回null；如果找到多条，抛出TooManyRowsException异常.
   *
   * @param elementType 持久化对象
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象，如果未找到任何数据，返回null；如果找到多条，抛出TooManyRowsException异常.
   */
  default <ID, T extends Persistent<ID>> T findOneByExample(Class<T> elementType,
      Example example) {
    List<T> result = findByExample(elementType, example, 0, 2);
    if (result == null || result.isEmpty()) {
      return null;
    }
    if (result.size() > 1) {
      throw new TooManyRowsException(result.size());
    }
    return result.get(0);
  }

  /**
   * 根据主键查找.
   *
   * @param elementType 持久化对象
   * @param id 主键
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 持久化对象，如果未找到任何数据，返回null
   */
  default <ID, T extends Persistent<ID>> T findById(Class<T> elementType, ID id) {
    return findById(elementType, id, Lists.newArrayList());
  }

  /**
   * 根据条件查找，并返回总数.
   *
   * @param elementType 持久化对象
   * @param example 查询参数的定义，包括查询条件、排序规则等
   * @param start 开始索引
   * @param limit 查询数量
   * @param <ID> 主键类型
   * @param <T> 持久化对象
   * @return 分页对象
   */
  default <ID, T extends Persistent<ID>> Page<T> page(Class<T> elementType, Example example,
      int start,
      int limit) {
    List<T> records = findByExample(elementType, example, start, limit);
    //如果records的数量小于limit，说明已经没有记录，直接计算总数
    if (records.size() > 0 && records.size() < limit) {
      int total = start + records.size();
      return Page.newInstance(total, records);
    }
    //通过数据库查询总数
    final int totalRecords = countByExample(elementType, example);
    return Page.newInstance(totalRecords, records);
  }


}
