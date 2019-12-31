package com.github.edgar615.jdbc.codegen.gen;

/**
 * 逻辑删除配置，借鉴了mybatis-plus
 */
public class LogicDeleteOptions {

  /**
   * 全局的逻辑删除字段
   */
  private String logicDeleteField;

  /**
   * 逻辑已删除值，默认为1，mybatis实际没有bool类型，只是我们持久层为了方便操作生成了bool
   */
  private int logicDeleteValue = 1;

  /**
   * 逻辑未删除值，默认为0
   */
  private int logicNotDeleteValue = 0;

  public LogicDeleteOptions(String logicDeleteField, int logicDeleteValue,
      int logicNotDeleteValue) {
    this.logicDeleteField = logicDeleteField;
    this.logicDeleteValue = logicDeleteValue;
    this.logicNotDeleteValue = logicNotDeleteValue;
  }

  public LogicDeleteOptions(String logicDeleteField) {
    this.logicDeleteField = logicDeleteField;
  }

  public String getLogicDeleteField() {
    return logicDeleteField;
  }

  public void setLogicDeleteField(String logicDeleteField) {
    this.logicDeleteField = logicDeleteField;
  }

  public int getLogicDeleteValue() {
    return logicDeleteValue;
  }

  public void setLogicDeleteValue(int logicDeleteValue) {
    this.logicDeleteValue = logicDeleteValue;
  }

  public int getLogicNotDeleteValue() {
    return logicNotDeleteValue;
  }

  public void setLogicNotDeleteValue(int logicNotDeleteValue) {
    this.logicNotDeleteValue = logicNotDeleteValue;
  }
}
