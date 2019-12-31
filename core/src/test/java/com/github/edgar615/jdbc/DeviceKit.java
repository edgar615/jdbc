package com.github.edgar615.jdbc;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;

/**
 * This class is generated by Jdbc code generator.
 * <p>
 * Table : device remarks: 设备表
 *
 * @author Jdbc Code Generator Date
 */
public class DeviceKit implements PersistentKit<Integer, Device> {


  @Override
  public List<String> fields() {
    return Lists.newArrayList("deviceId",
        "userId",
        "parentId",
        "username",
        "isVirtual",
        "companyCode",
        "name",
        "barcode",
        "macAddress",
        "encryptKey",
        "type",
        "state",
        "location",
        "deviceCode",
        "manufacturerCode",
        "manufacturerName",
        "description",
        "productVersion",
        "zigbeeVersion",
        "zigbeeMacAddress",
        "mainFeature",
        "wifiFirm",
        "wifiVersion",
        "serverAddress",
        "publicIp",
        "isOnline",
        "addOn",
        "createdOn");
  }

  @Override
  public String primaryField() {
    return "deviceId";
  }

  @Override
  public void toMap(Device entity, Map<String, Object> map) {
    if (map == null) {
      return;
    }
    map.put("barcode", entity.getBarcode());
    map.put("companyCode", entity.getCompanyCode());
    map.put("isOnline", entity.getIsOnline());
  }

  @Override
  public void fromMap(Map<String, Object> map, Device entity) {

  }


  /* START Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/
  /* END Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/


}