package com.github.edgar615.db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

public class DeleteBuilderTest {

  @Test
  public void testSimple() {
    DeleteBuilder deleteBuilder = DeleteBuilder.create("device");
    SQLBindings sqlBindings = deleteBuilder.build();
    Assert.assertEquals("delete from device", sqlBindings.sql());
    Assert.assertTrue(sqlBindings.bindings().isEmpty());
  }


  @Test
  public void testOneWhere() {
    DeleteBuilder deleteBuilder = DeleteBuilder.create("device")
        .and(Predicates.equalsTo("device_id", 1L));
    SQLBindings sqlBindings = deleteBuilder.build();
    Assert.assertEquals("delete from device where device_id = ?", sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().size());
    Assert.assertEquals(1L, sqlBindings.bindings().get(0));
  }

}
