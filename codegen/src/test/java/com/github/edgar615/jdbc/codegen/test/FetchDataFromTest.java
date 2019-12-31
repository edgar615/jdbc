package com.github.edgar615.jdbc.codegen.test;

import com.github.edgar615.jdbc.codegen.gen.CodegenOptions;
import com.github.edgar615.jdbc.codegen.gen.DaoOptions;
import com.github.edgar615.jdbc.codegen.gen.Generator;
import com.github.edgar615.jdbc.codegen.gen.LogicDeleteOptions;
import com.github.edgar615.jdbc.codegen.gen.MybatisOptions;

/**
 * Created by Administrator on 2015/6/9.
 */
public class FetchDataFromTest {

  public static void main(String[] args) throws Exception {
    CodegenOptions options = new CodegenOptions()
        .setUsername("root")//用户名
        .setPassword("123456") //密码
        .setHost("localhost") //数据库地址
        .setPort(3306) //端口，默认值3306
        .setDatabase("test") //数据库
        .setSrcFolderPath("spring-support/src/test/java")//生成JAVA文件的存放目录
        .setDomainPackage("com.github.edgar615.jdbc.spring.entity")//domain类的包名
        .setIgnoreColumnsStr("created_on,updated_on")
        .setDaoOptions(new DaoOptions().setDaoPackage("com.github.edgar615.jdbc.spring.dao").addLogicDeleteOptions("user", new LogicDeleteOptions("state")))
        .setGenRule(true)
        .addGenTable("user")
        .addVersion("user", "add_time");
    new Generator(options).generate();

  }

}
