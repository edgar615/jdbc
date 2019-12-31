package com.github.edgar615.jdbc.codegen.gen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用于生成DAO的参数.为了避免后期扩展，单独列出来.
 *
 * @author Edgar
 * @create 2018-08-31 19:31
 **/
public class DaoOptions {

  private static final String DEFAULT_DAO_PACKAGE = "com.github.edgar615.codegen.dao";
  private static final boolean DEFAULT_SUPPORT_SPRING = true;
  private static final boolean DEFAULT_GEN_IMPL = true;

  private String daoPackage = DEFAULT_DAO_PACKAGE;

  private boolean supportSpring = DEFAULT_SUPPORT_SPRING;

  private boolean genImpl = DEFAULT_GEN_IMPL;

  private final Map<String, LogicDeleteOptions> logicDeleteOptions = new HashMap<>();

  public String getDaoPackage() {
    return daoPackage;
  }

  public DaoOptions setDaoPackage(String daoPackage) {
    this.daoPackage = daoPackage;
    return this;
  }

  public boolean isSupportSpring() {
    return supportSpring;
  }

  public DaoOptions setSupportSpring(boolean supportSpring) {
    this.supportSpring = supportSpring;
    return this;
  }

  public boolean isGenImpl() {
    return genImpl;
  }

  public DaoOptions setGenImpl(boolean genImpl) {
    this.genImpl = genImpl;
    return this;
  }

  public DaoOptions addLogicDeleteOptions(String tableName, LogicDeleteOptions options) {
    Objects.requireNonNull(tableName);
    Objects.requireNonNull(options);
    logicDeleteOptions.put(tableName, options);
    return this;
  }

  public Map<String, LogicDeleteOptions> getLogicDeleteOptions() {
    return logicDeleteOptions;
  }
}
