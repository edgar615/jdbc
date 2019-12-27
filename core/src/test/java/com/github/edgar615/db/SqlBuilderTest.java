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

import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.search.Example;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Edgar on 2017/5/18.
 *
 * @author Edgar  Date 2017/5/18
 */
public class SqlBuilderTest {

  @Test
  public void testInsert() {

    Device device = new Device();
    device.setBarcode("barcode");
    device.setCompanyCode(0);
    SQLBindings sqlBindings = SqlBuilder.insert(device);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("insert into device(barcode,company_code) values(?,?)", sqlBindings.sql());
    Assert.assertEquals("barcode", sqlBindings.bindings().get(0));
    Assert.assertEquals(0, sqlBindings.bindings().get(1));
  }


  @Test
  public void testFullInsert() {

    Device device = new Device();
    device.setBarcode("barcode");
    device.setCompanyCode(0);
    SQLBindings sqlBindings = SqlBuilder.fullInsertSql(device);
    String expected = "insert into device(device_id,user_id,parent_id,username,is_virtual,company_code,name,barcode,mac_address,encrypt_key,type,state,location,device_code,manufacturer_code,manufacturer_name,description,product_version,zigbee_version,zigbee_mac_address,main_feature,wifi_firm,wifi_version,server_address,public_ip,is_online,add_on,created_on) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    Assert.assertEquals(expected, sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("barcode", sqlBindings.bindings().get(7));
    Assert.assertEquals(0, sqlBindings.bindings().get(5));
  }

  @Test
  public void testDeleteById() {
    SQLBindings sqlBindings = SqlBuilder.deleteById(Device.class, 1);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("delete from device where device_id = ?", sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }


  @Test
  public void testDeleteByExample() {
    Example example = Example.create()
        .isNull("macAddress");
    SQLBindings sqlBindings = SqlBuilder.deleteByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("delete from device where mac_address is null",
        sqlBindings.sql());
    Assert.assertEquals(0, sqlBindings.bindings().size());
  }

  @Test
  public void testUpdateById() {
    Device device = new Device();
    device.setBarcode("barcode");
    device.setCompanyCode(0);
    SQLBindings sqlBindings = SqlBuilder.updateById(device, null, null, 1);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("update device set barcode = ?,company_code = ? where device_id = ?",
        sqlBindings.sql());
    Assert.assertEquals("barcode", sqlBindings.bindings().get(0));
    Assert.assertEquals(0, sqlBindings.bindings().get(1));
    Assert.assertEquals(1, sqlBindings.bindings().get(2));
  }

  @Test
  public void testUpdateById2() {
    Device device = new Device();
    device.setBarcode("barcode");
    device.setCompanyCode(0);
    Map<String, Number> addOrSub = ImmutableMap.of("parentId", 1, "deviceCode", -2,
        "type", new BigDecimal("1.0"), "addOn", new BigDecimal("-1.1"));
    List<String> fiedls = Lists.newArrayList("manufacturerName");
    SQLBindings sqlBindings = SqlBuilder.updateById(device, addOrSub, fiedls, 1);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals(
        "update device set barcode = ?,company_code = ?,parent_id = parent_id + 1,device_code = device_code - 2,type = type + 1.0,add_on = add_on - 1.1,manufacturer_name = null where device_id = ?",
        sqlBindings.sql());
    Assert.assertEquals("barcode", sqlBindings.bindings().get(0));
    Assert.assertEquals(0, sqlBindings.bindings().get(1));
    Assert.assertEquals(1, sqlBindings.bindings().get(2));
  }

  @Test
  public void testUpdateEmptyShouldFailed() {
    Device device = new Device();
    SQLBindings sqlBindings = SqlBuilder.updateById(device, null, null, 1);
    Assert.assertNull(sqlBindings);
  }

  @Test
  public void testFindById() {
    SQLBindings sqlBindings = SqlBuilder.findById(Device.class, 1);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("select " + allColumn() + " from device where device_id = ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testFindByIdWithFields() {
    List<String> fields = new ArrayList<>();
    fields.add("deviceId");
    fields.add("companyCode");
    fields.add(Randoms.randomAlphabet(20));
    SQLBindings sqlBindings = SqlBuilder.findById(Device.class, 1, fields);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("select device_id, company_code from device where device_id = ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testSetNull() {
    SQLBindings sqlBindings = SqlBuilder.updateById(new Device(), Maps.newHashMap(),
        Lists.newArrayList("abc", "userId", "parentId"), 1);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("update device set user_id = null,parent_id = null where device_id = ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testFindByExample() {
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF")
        .regexp("macAddress", "^F")
        .desc("userId");
    SQLBindings sqlBindings = SqlBuilder.findByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("select " + allColumn()
            + " from device where type in (?,?,?) and mac_address like ? and mac_address regexp ? order by user_id desc",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testFindByExampleWithDistinct() {
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF")
        .desc("userId")
        .withDistinct();
    SQLBindings sqlBindings = SqlBuilder.findByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("select distinct " + allColumn()
            + " from device where type in (?,?,?) and mac_address like ? order by user_id desc",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testFindByExampleWithField() {
    List<String> fields = new ArrayList<>();
    fields.add("deviceId");
    fields.add("companyCode");
    fields.add(Randoms.randomAlphabet(20));
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF");
    example.addFields(fields);
    SQLBindings sqlBindings = SqlBuilder.findByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals(
        "select device_id, company_code from device where type in (?,?,?) and mac_address like ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testFindByExampleWithFieldAndDistinct() {
    List<String> fields = new ArrayList<>();
    fields.add("deviceId");
    fields.add("companyCode");
    fields.add(Randoms.randomAlphabet(20));
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF")
        .withDistinct();
    example.addFields(fields);
    SQLBindings sqlBindings = SqlBuilder.findByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals(
        "select distinct device_id, company_code from device where type in (?,?,?) and mac_address like ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testFindByExampleWithErrorField() {
    List<String> fields = new ArrayList<>();
    fields.add(Randoms.randomAlphabet(20));
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF");
    example.addFields(fields);
    SQLBindings sqlBindings = SqlBuilder.findByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals(
        "select " + allColumn() + " from device where type in (?,?,?) and mac_address like ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testFindByExampleLimit() {
    List<String> fields = new ArrayList<>();
    fields.add("deviceId");
    fields.add("companyCode");
    fields.add(Randoms.randomAlphabet(20));
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF");
    SQLBindings sqlBindings = SqlBuilder.findByExample(Device.class, example, 5, 10);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("select " + allColumn()
            + " from device where type in (?,?,?) and mac_address like ? limit ?, ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testCountByExample() {
    List<String> fields = new ArrayList<>();
    fields.add("deviceId");
    fields.add("companyCode");
    fields.add(Randoms.randomAlphabet(20));
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF")
        .asc("userId")
        .addFields(fields);
    SQLBindings sqlBindings = SqlBuilder.countByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals("select count(*) from device where type in (?,?,?) and mac_address like ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  @Test
  public void testCountByExampleWithDistinct() {
    List<String> fields = new ArrayList<>();
    fields.add("deviceId");
    fields.add("companyCode");
    fields.add(Randoms.randomAlphabet(20));
    Example example = Example.create().in("type", Lists.newArrayList(1, 2, 3))
        .startsWith("macAddress", "FFFF")
        .asc("userId")
        .addFields(fields)
        .withDistinct();
    SQLBindings sqlBindings = SqlBuilder.countByExample(Device.class, example);
    System.out.println(sqlBindings.sql());
    System.out.println(sqlBindings.bindings());
    Assert.assertEquals(
        "select count(distinct(device_id, company_code)) from device where type in (?,?,?) and mac_address like ?",
        sqlBindings.sql());
    Assert.assertEquals(1, sqlBindings.bindings().get(0));
  }

  private String allColumn() {
    List<String> fields = new DeviceKit().fields()
        .stream().map(f -> StringUtils.underscoreName(f))
        .collect(Collectors.toList());
    return Joiner.on(", ").join(fields);
  }
}
