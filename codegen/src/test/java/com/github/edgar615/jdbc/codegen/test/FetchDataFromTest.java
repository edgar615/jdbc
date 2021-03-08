package com.github.edgar615.jdbc.codegen.test;

import com.github.edgar615.jdbc.codegen.gen.*;

/**
 * Created by Administrator on 2015/6/9.
 */
public class FetchDataFromTest {

  public static void main(String[] args) throws Exception {
    CodegenOptions options = new CodegenOptions()
        .setUsername("root")//用户名
        .setPassword("123456") //密码
        .setHost("192.168.159.131") //数据库地址
        .setPort(3306) //端口，默认值3306
        .setDatabase("ds0") //数据库
        .setSrcFolderPath("spring-support/src/test/java")//生成JAVA文件的存放目录
        .setDomainPackage("com.github.edgar615.jdbc.spring.entity")//domain类的包名
        .setIgnoreColumnsStr("created_on,updated_on")
        .addGenTable("user")
        .addVersion("user", "add_time");
    Generators.create(options)
        .addGenerator(DomainGenerator.create(options, new DomainOptions()))
        .addGenerator(SqlSupportGenerator.create(options))
//        .addGenerator(DomainRuleGenerator.create(options))
        .addGenerator(DaoGenerator.create(options, new DaoOptions().setDaoPackage("com.github.edgar615.jdbc.spring.dao")))
        .addGenerator(DaoImplGenerator.create(options, new DaoOptions().setDaoPackage("com.github.edgar615.jdbc.spring.dao")))
        .generate();

  }

}
