package com.github.edgar615.jdbc.codegen.gen;

import com.github.edgar615.jdbc.codegen.db.Table;

public class DaoGenerator implements Generator {

  private final CodegenOptions codegenOptions;

  private final DaoOptions daoOptions;

  private static final String tplFile = "tpl/dao.hbs";

  private DaoGenerator(CodegenOptions codegenOptions,
      DaoOptions daoOptions) {
    this.codegenOptions = codegenOptions;
    this.daoOptions = daoOptions;
  }

  public static DaoGenerator create(CodegenOptions codegenOptions,
      DaoOptions daoOptions) {
    return new DaoGenerator(codegenOptions, daoOptions);
  }

  @Override
  public void generate(Table table) {
    Codegen daoGen = new Codegen(codegenOptions.getSrcFolderPath(),
        daoOptions.getDaoPackage(), "Dao", tplFile);
    daoGen.addVariable("domainPackage", codegenOptions.getDomainPackage());
    daoGen.addImport("com.github.edgar615.dao.BaseDao");
    if (!codegenOptions.getDomainPackage().equals(daoOptions.getDaoPackage())) {
      daoGen.addImport(codegenOptions.getDomainPackage() + "." + table.getUpperCamelName());
    }

    daoGen.genCode(table);
  }
}
