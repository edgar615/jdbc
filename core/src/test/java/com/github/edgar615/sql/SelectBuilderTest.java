package com.github.edgar615.sql;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class SelectBuilderTest {

  @Test
  public void testSimple() {
    SelectBuilder selectBuilder = SelectBuilder.create("device");
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals("select * from device", sqlBindings.sql());
    Assert.assertTrue(sqlBindings.bindings().isEmpty());
  }

  @Test
  public void testLimit() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10);
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals("select * from device limit ?", sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().size());
    Assert.assertEquals(10L, sqlBindings.bindings().get(0));
  }

  @Test
  public void testLimitOffset() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5);
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals("select * from device limit ?, ?", sqlBindings.sql());
    Assert.assertEquals(2, sqlBindings.bindings().size());
    Assert.assertEquals(5L, sqlBindings.bindings().get(0));
    Assert.assertEquals(10L, sqlBindings.bindings().get(1));
  }

  @Test
  public void testOneWhere() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals("select * from device where device_id = ? limit ?, ?", sqlBindings.sql());
    Assert.assertEquals(3, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals(5L, sqlBindings.bindings().get(1));
    Assert.assertEquals(10L, sqlBindings.bindings().get(2));
  }

  @Test
  public void testTwoAnd() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.startWith("mac_address", "000F"));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert
        .assertEquals("select * from device where device_id = ? and mac_address like ? limit ?, ?",
            sqlBindings.sql());
    Assert.assertEquals(4, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals(5L, sqlBindings.bindings().get(2));
    Assert.assertEquals(10L, sqlBindings.bindings().get(3));
  }

  @Test
  public void testTwoOr() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .or(Predicates.startWith("mac_address", "000F"));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals("select * from device where device_id = ? or mac_address like ? limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(4, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals(5L, sqlBindings.bindings().get(2));
    Assert.assertEquals(10L, sqlBindings.bindings().get(3));
  }

  @Test
  public void testOneAndTowOr() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.or(Predicates.startWith("mac_address", "000F"),
            Predicates.startWith("mac_address", "000E")));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device where device_id = ? and ( mac_address like ? or mac_address like ? ) limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(5, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals("000E%", sqlBindings.bindings().get(2));
    Assert.assertEquals(5L, sqlBindings.bindings().get(3));
    Assert.assertEquals(10L, sqlBindings.bindings().get(4));
  }

  @Test
  public void testOneOrTowAnd() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.startWith("mac_address", "000F"))
        .or(Predicates.and(Predicates.equalsTo("device_id", 1L),
            Predicates.startWith("mac_address", "000E")));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device where mac_address like ? or ( device_id = ? and mac_address like ? ) limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(5, sqlBindings.bindings().size());
    Assert.assertEquals("000F%", sqlBindings.bindings().get(0));
    Assert.assertEquals(1L, sqlBindings.bindings().get(1));
    Assert.assertEquals("000E%", sqlBindings.bindings().get(2));
    Assert.assertEquals(5L, sqlBindings.bindings().get(3));
    Assert.assertEquals(10L, sqlBindings.bindings().get(4));
  }

  @Test
  public void testTowAndTowOr() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates
            .or(Predicates.equalsTo("device_id", 1L), Predicates.equalsTo("device_id", 8L)))
        .and(Predicates.or(Predicates.startWith("mac_address", "000F"),
            Predicates.startWith("mac_address", "000E")));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device where ( device_id = ? or device_id = ? ) and ( mac_address like ? or mac_address like ? ) limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(6, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals(8L, sqlBindings.bindings().get(1));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(2));
    Assert.assertEquals("000E%", sqlBindings.bindings().get(3));
    Assert.assertEquals(5L, sqlBindings.bindings().get(4));
    Assert.assertEquals(10L, sqlBindings.bindings().get(5));
  }

  @Test
  public void testTableAlias() {
    SelectBuilder selectBuilder = SelectBuilder.create("device d")
        .limit(10, 5)
        .and(Predicates
            .or(Predicates.equalsTo("device_id", 1L), Predicates.equalsTo("device_id", 8L)))
        .and(Predicates.or(Predicates.startWith("mac_address", "000F"),
            Predicates.startWith("mac_address", "000E")));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device d where ( device_id = ? or device_id = ? ) and ( mac_address like ? or mac_address like ? ) limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(6, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals(8L, sqlBindings.bindings().get(1));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(2));
    Assert.assertEquals("000E%", sqlBindings.bindings().get(3));
    Assert.assertEquals(5L, sqlBindings.bindings().get(4));
    Assert.assertEquals(10L, sqlBindings.bindings().get(5));
  }

  @Test
  public void testAlias() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates
            .or(Predicates.equalsTo("d.device_id", 1L), Predicates.equalsTo("d.device_id", 8L)))
        .and(Predicates.or(Predicates.startWith("d.mac_address", "000F"),
            Predicates.startWith("d.mac_address", "000E")));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device where ( d.device_id = ? or d.device_id = ? ) and ( d.mac_address like ? or d.mac_address like ? ) limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(6, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals(8L, sqlBindings.bindings().get(1));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(2));
    Assert.assertEquals("000E%", sqlBindings.bindings().get(3));
    Assert.assertEquals(5L, sqlBindings.bindings().get(4));
    Assert.assertEquals(10L, sqlBindings.bindings().get(5));
  }

  @Test
  public void testColumn() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .column("device_id")
        .column("mac_address, owner_id as o")
        .column("min(add_on) as min_add_on")
        .limit(10, 5)
        .and(Predicates
            .or(Predicates.equalsTo("device_id", 1L), Predicates.equalsTo("device_id", 8L)))
        .and(Predicates.or(Predicates.startWith("mac_address", "000F"),
            Predicates.startWith("mac_address", "000E")));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select device_id, mac_address, owner_id as o, min(add_on) as min_add_on from device where ( device_id = ? or device_id = ? ) and ( mac_address like ? or mac_address like ? ) limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(6, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals(8L, sqlBindings.bindings().get(1));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(2));
    Assert.assertEquals("000E%", sqlBindings.bindings().get(3));
    Assert.assertEquals(5L, sqlBindings.bindings().get(4));
    Assert.assertEquals(10L, sqlBindings.bindings().get(5));
  }

  @Test
  public void testDistinct() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates
            .or(Predicates.equalsTo("device_id", 1L), Predicates.equalsTo("device_id", 8L)))
        .and(Predicates.or(Predicates.startWith("mac_address", "000F"),
            Predicates.startWith("mac_address", "000E")))
        .distinct()
        .forUpdate();
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select distinct * from device where ( device_id = ? or device_id = ? ) and ( mac_address like ? or mac_address like ? ) limit ?, ? for update",
        sqlBindings.sql());
    Assert.assertEquals(6, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals(8L, sqlBindings.bindings().get(1));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(2));
    Assert.assertEquals("000E%", sqlBindings.bindings().get(3));
    Assert.assertEquals(5L, sqlBindings.bindings().get(4));
    Assert.assertEquals(10L, sqlBindings.bindings().get(5));
  }

  @Test
  public void testGroupBy() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.startWith("mac_address", "000F"))
        .groupBy("owner_id");
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device where device_id = ? and mac_address like ? group by owner_id limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(4, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals(5L, sqlBindings.bindings().get(2));
    Assert.assertEquals(10L, sqlBindings.bindings().get(3));
  }

  @Test
  public void testHaving() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.startWith("mac_address", "000F"))
        .groupBy("owner_id")
        .having(Predicates.greaterThan("count(device_id)", 5));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device where device_id = ? and mac_address like ? group by owner_id having count(device_id) > ? limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(5, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals(5, sqlBindings.bindings().get(2));
    Assert.assertEquals(5L, sqlBindings.bindings().get(3));
    Assert.assertEquals(10L, sqlBindings.bindings().get(4));
  }

  @Test
  public void testOrderBy() {
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.startWith("mac_address", "000F"))
        .groupBy("owner_id")
        .having(Predicates.greaterThan("count(device_id)", 5))
        .asc("device_id")
        .desc("device_count");
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device where device_id = ? and mac_address like ? group by owner_id having count(device_id) > ? order by device_id asc, device_count desc limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(5, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals(5, sqlBindings.bindings().get(2));
    Assert.assertEquals(5L, sqlBindings.bindings().get(3));
    Assert.assertEquals(10L, sqlBindings.bindings().get(4));
  }

  @Test
  public void testAllPredicate() {
    SelectBuilder selectBuilder = SelectBuilder.create("device d")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.notEqualsTo("device_id", 5L))
        .and(Predicates.notEqualsToExpr("device_id", "shop_id"))
        .and(Predicates.lessThan("device_id", 100L))
        .and(Predicates.lessThanExpr("device_id", "part_id"))
        .and(Predicates.greaterThan("device_id", 0L))
        .and(Predicates.greaterThanExpr("device_id", "part_id"))
        .and(Predicates.lessThanOrEqualToExpr("device_id", "shop_id"))
        .and(Predicates.lessThanOrEqualTo("add_on", 10000L))
        .and(Predicates.greaterThanOrEqualToExpr("device_id", "owner_id"))
        .and(Predicates.greaterThanOrEqualTo("update_on", 20000L))
        .and(Predicates.startWith("mac_address", "000F"))
        .and(Predicates.endWith("mac_address", "0086"))
        .and(Predicates.contains("mac_address", "45"))
        .and(Predicates.likeExpr("mac_address", "%0086%"))
        .and(Predicates.regexp("mac_address", "ef"))
        .and(Predicates.in("device_id", Lists.newArrayList(1L, 2L, 3L, 4L)))
        .and(Predicates.notIn("device_id", Lists.newArrayList(100L, 90L)))
        .and(Predicates.isNotNull("mac_address"))
        .and(Predicates.isNull("encrypt_key"));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device d where device_id = ? and device_id <> ? and device_id <> shop_id and device_id < ? and device_id < part_id and device_id > ? and device_id > part_id and device_id <= shop_id and add_on <= ? and device_id >= owner_id and update_on >= ? and mac_address like ? and mac_address like ? and mac_address like ? and mac_address like %0086% and mac_address regexp ? and device_id in (?,?,?,?) and device_id not in (?,?) and mac_address is not null and encrypt_key is null limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(18, sqlBindings.bindings().size());
  }

  @Test
  public void testJoin() {
    SelectBuilder selectBuilder = SelectBuilder.create("device d")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.startWith("mac_address", "000F"))
        .join("user u on d.owner_id = u.user_id")
        .join("company c on d.company_id = c.company_id")
        .asc("device_id");
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device d join user u on d.owner_id = u.user_id join company c on d.company_id = c.company_id where device_id = ? and mac_address like ? order by device_id asc limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(4, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals(5L, sqlBindings.bindings().get(2));
    Assert.assertEquals(10L, sqlBindings.bindings().get(3));
  }

  @Test
  public void testLeftJoin() {
    SelectBuilder selectBuilder = SelectBuilder.create("device d")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", 1L))
        .and(Predicates.startWith("mac_address", "000F"))
        .leftJoin("user u on d.owner_id = u.user_id")
        .leftJoin("company c on d.company_id = c.company_id")
        .asc("device_id");
    SQLBindings sqlBindings = selectBuilder.build();
    Assert.assertEquals(
        "select * from device d left join user u on d.owner_id = u.user_id left join company c on d.company_id = c.company_id where device_id = ? and mac_address like ? order by device_id asc limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(4, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals("000F%", sqlBindings.bindings().get(1));
    Assert.assertEquals(5L, sqlBindings.bindings().get(2));
    Assert.assertEquals(10L, sqlBindings.bindings().get(3));
  }


  @Test
  public void testCondition() {
    Map<String, Object> params = new HashMap<>();
    params.put("deviceId", 1L);
    SelectBuilder selectBuilder = SelectBuilder.create("device")
        .limit(10, 5)
        .and(Predicates.equalsTo("device_id", params.get("deviceId")), Conditions.notNull(params.get("deviceId")))
        .and(Predicates.startWith("mac_address", "000F"), Conditions.notNull(params.get("macAddress")));
    SQLBindings sqlBindings = selectBuilder.build();
    Assert
        .assertEquals("select * from device where device_id = ? limit ?, ?",
            sqlBindings.sql());
    Assert.assertEquals(3, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
    Assert.assertEquals(5L, sqlBindings.bindings().get(1));
    Assert.assertEquals(10L, sqlBindings.bindings().get(2));
  }
}
