package com.github.edgar615.jdbc.spring;

import com.github.edgar615.entity.Persistent;
import com.google.common.base.MoreObjects;
import java.util.Date;

/**
 * This class is generated by Jdbc code generator.
 * <p>
 * Table : device remarks: 设备表
 *
 * @author Jdbc Code Generator Date
 */
public class Device implements Persistent<Integer> {

  private static final long serialVersionUID = 1L;

  /**
   * Column : device_id remarks: default: isNullable: false isAutoInc: true isPrimary: true type: 4
   * size: 10
   */
  private Integer deviceId;

  /**
   * Column : parent_id remarks: default: isNullable: true isAutoInc: false isPrimary: false type: 4
   * size: 10
   */
  private Integer parentId;

  /**
   * Column : is_virtual remarks: default: isNullable: true isAutoInc: false isPrimary: false type:
   * -6 size: 3
   */
  private Integer isVirtual;

  /**
   * Column : company_code remarks: default: isNullable: false isAutoInc: false isPrimary: false
   * type: 4 size: 10
   */
  private Integer companyCode;

  /**
   * Column : name remarks: default: isNullable: true isAutoInc: false isPrimary: false type: 12
   * size: 64
   */
  private String name;

  /**
   * Column : barcode remarks: default: isNullable: true isAutoInc: false isPrimary: false type: 1
   * size: 19
   */
  private String barcode;

  /**
   * Column : mac_address remarks: default: isNullable: true isAutoInc: false isPrimary: false type:
   * 1 size: 12
   */
  private String macAddress;

  /**
   * Column : encrypt_key remarks: default: isNullable: false isAutoInc: false isPrimary: false
   * type: 1 size: 16
   */
  private String encryptKey;

  /**
   * Column : type remarks: default: isNullable: true isAutoInc: false isPrimary: false type: 4
   * size: 10
   */
  private Integer type;


  /**
   * Column : is_online remarks: default: isNullable: true isAutoInc: false isPrimary: false type:
   * -6 size: 3
   */
  private Integer isOnline;

  /**
   * Column : add_on remarks: default: isNullable: true isAutoInc: false isPrimary: false type: 4
   * size: 10
   */
  private Integer addOn;

  /**
   * Column : created_on remarks: default: CURRENT_TIMESTAMP isNullable: true isAutoInc: false
   * isPrimary: false type: 93 size: 19
   */
  private Date createdOn;

  public Integer setDeviceId(Integer deviceId) {
    return this.deviceId = deviceId;
  }

  public Integer setParentId(Integer parentId) {
    return this.parentId = parentId;
  }

  public Integer setIsVirtual(Integer isVirtual) {
    return this.isVirtual = isVirtual;
  }

  public Integer setCompanyCode(Integer companyCode) {
    return this.companyCode = companyCode;
  }

  public String setName(String name) {
    return this.name = name;
  }

  public String setBarcode(String barcode) {
    return this.barcode = barcode;
  }

  public String setMacAddress(String macAddress) {
    return this.macAddress = macAddress;
  }

  public String setEncryptKey(String encryptKey) {
    return this.encryptKey = encryptKey;
  }

  public Integer setType(Integer type) {
    return this.type = type;
  }

  public Integer setIsOnline(Integer isOnline) {
    return this.isOnline = isOnline;
  }

  public Integer setAddOn(Integer addOn) {
    return this.addOn = addOn;
  }

  public Date setCreatedOn(Date createdOn) {
    return this.createdOn = createdOn;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("Company")
        .add("deviceId", deviceId)
        .add("parentId", parentId)
        .add("isVirtual", isVirtual)
        .add("companyCode", companyCode)
        .add("name", name)
        .add("barcode", barcode)
        .add("macAddress", macAddress)
        .add("encryptKey", encryptKey)
        .add("type", type)
        .add("isOnline", isOnline)
        .add("addOn", addOn)
        .add("createdOn", createdOn)
        .toString();
  }

  @Override
  public Integer id() {
    return deviceId;
  }

  public Integer getDeviceId() {
    return deviceId;
  }

  public Integer getParentId() {
    return parentId;
  }

  public Integer getIsVirtual() {
    return isVirtual;
  }

  public Integer getCompanyCode() {
    return companyCode;
  }

  public String getName() {
    return name;
  }

  public String getBarcode() {
    return barcode;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public String getEncryptKey() {
    return encryptKey;
  }

  public Integer getType() {
    return type;
  }

  public Integer getIsOnline() {
    return isOnline;
  }

  public Integer getAddOn() {
    return addOn;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  @Override
  public void setId(Integer id) {
    this.deviceId = id;
  }

  @Override
  public void setGeneratedKey(Number key) {
    this.deviceId = key.intValue();
  }

  /* START Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/
  /* END Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/


}
