package com.github.edgar615.jdbc;

import org.junit.Assert;
import org.junit.Test;

/**
 * Persistent的测试类.
 *
 * @author Edgar
 * @create 2018-09-06 15:15
 **/
public class PersistentTest {

  @Test
  public void testPrimaryKey() {
    String primaryKey = new DeviceJdbcSqlSupport().primaryField();
    Assert.assertEquals("deviceId", primaryKey);
  }

}
