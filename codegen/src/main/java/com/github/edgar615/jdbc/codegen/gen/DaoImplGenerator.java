package com.github.edgar615.jdbc.codegen.gen;

import com.github.edgar615.jdbc.codegen.db.Table;

public class DaoImplGenerator implements Generator {

  private final CodegenOptions codegenOptions;

  private final DaoOptions daoOptions;

  private static final String tplFile = "tpl/daoImpl.hbs";

  private DaoImplGenerator(CodegenOptions codegenOptions,
      DaoOptions daoOptions) {
    this.codegenOptions = codegenOptions;
    this.daoOptions = daoOptions;
  }

  public static DaoImplGenerator create(CodegenOptions codegenOptions,
      DaoOptions daoOptions) {
    return new DaoImplGenerator(codegenOptions, daoOptions);
  }

  @Override
  public void generate(Table table) {
    String daoImplPackage = daoOptions.getDaoImplPackage();
    Codegen daoImplGen = new Codegen(codegenOptions.getSrcFolderPath(),
        daoImplPackage, "DaoImpl", tplFile);
    daoImplGen.addVariable("daoPackage", daoOptions.getDaoPackage());
    daoImplGen.addVariable("domainPackage", codegenOptions.getDomainPackage());
    daoImplGen.addVariable("supportSpring", daoOptions.isSupportSpring());
    if (daoOptions.getLogicDeleteOptions().get(table.getName()) != null) {
      LogicDeleteOptions logicDeleteOptions = daoOptions.getLogicDeleteOptions()
          .get(table.getName());
      if (logicDeleteOptions != null) {
        daoImplGen.addVariable("supportLogicDelete", true);
        daoImplGen.addVariable("logicDeleteField", logicDeleteOptions.getLogicDeleteField());
        daoImplGen.addVariable("logicDeleteValue", logicDeleteOptions.getLogicDeleteValue());
        daoImplGen.addVariable("logicNotDeleteValue", logicDeleteOptions.getLogicNotDeleteValue());
        daoImplGen.addImport("com.github.edgar615.util.search.Example")
            .addImport("java.util.List");
      }
    }
    daoImplGen.addImport("com.github.edgar615.dao.BaseDaoImpl")
        .addImport("com.github.edgar615.jdbc.Jdbc");

    if (!codegenOptions.getDomainPackage().equals(daoImplPackage)) {
      daoImplGen.addImport(codegenOptions.getDomainPackage() + "." + table.getUpperCamelName());
    }

    daoImplGen.addImport(
        daoOptions.getDaoPackage() + "." + table.getUpperCamelName() + "Dao");
    if (daoOptions.isSupportSpring()) {
      daoImplGen.addImport("org.springframework.stereotype.Service");
    }
    daoImplGen.genCode(table);
  }
}
