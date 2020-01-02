package com.github.edgar615.jdbc.codegen.test;

import com.github.edgar615.jdbc.codegen.gen.CodegenOptions;
import com.github.edgar615.jdbc.codegen.gen.DaoGenerator;
import com.github.edgar615.jdbc.codegen.gen.DaoImplGenerator;
import com.github.edgar615.jdbc.codegen.gen.DaoOptions;
import com.github.edgar615.jdbc.codegen.gen.DomainGenerator;
import com.github.edgar615.jdbc.codegen.gen.DomainKitGenerator;
import com.github.edgar615.jdbc.codegen.gen.DomainOptions;
import com.github.edgar615.jdbc.codegen.gen.DomainRuleGenerator;
import com.github.edgar615.jdbc.codegen.gen.Generators;

/**
 * Created by Administrator on 2015/6/9.
 */
public class FetchDataFromTest {

  public static void main(String[] args) throws Exception {
    CodegenOptions options = new CodegenOptions()
        .setUsername("root")//用户名
        .setPassword("123456") //密码
        .setHost("127.0.0.1") //数据库地址
        .setPort(3306) //端口，默认值3306
        .setDatabase("test") //数据库
        .setSrcFolderPath("spring-support/src/test/java")//生成JAVA文件的存放目录
        .setDomainPackage("com.github.edgar615.jdbc.spring.entity")//domain类的包名
        .setIgnoreColumnsStr("created_on,updated_on")
        .addGenTable("user")
        .addVersion("user", "add_time");
    Generators.create(options)
        .addGenerator(DomainGenerator.create(options, new DomainOptions()))
        .addGenerator(DomainKitGenerator.create(options))
        .addGenerator(DomainRuleGenerator.create(options))
        .addGenerator(DaoGenerator.create(options, new DaoOptions().setDaoPackage("com.github.edgar615.jdbc.spring.dao")))
        .addGenerator(DaoImplGenerator.create(options, new DaoOptions().setDaoPackage("com.github.edgar615.jdbc.spring.dao")))
        .generate();

  }

}
