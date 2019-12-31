package com.github.edgar615.jdbc.codegen.test;

import com.github.edgar615.jdbc.codegen.gen.CodegenOptions;
import com.github.edgar615.jdbc.codegen.gen.DaoOptions;
import com.github.edgar615.jdbc.codegen.gen.Generator;
import com.github.edgar615.jdbc.codegen.gen.MybatisOptions;

/**
 * Created by Administrator on 2015/6/9.
 */
public class FetchDataFromTest {

  public static void main(String[] args) throws Exception {
    CodegenOptions options = new CodegenOptions()
        .setUsername("tabao")//用户名
        .setPassword("Nq2KB@Wcywy") //密码
        .setHost("192.168.1.210") //数据库地址
        .setPort(3306) //端口，默认值3306
        .setDatabase("tabao") //数据库
        .setSrcFolderPath("mybatis-spring-support/src/test/java")//生成JAVA文件的存放目录
        .setDomainPackage("com.github.edgar615.jdbc.spring.entity")//domain类的包名
        .setIgnoreColumnsStr("created_on,updated_on")
//        .setDaoOptions(new DaoOptions().setDaoPackage("com.github.edgar615.jdbc.spring.dao"))
        .setGenRule(true)
        .addGenTable("user");
    new Generator(options).generate();

  }

}
