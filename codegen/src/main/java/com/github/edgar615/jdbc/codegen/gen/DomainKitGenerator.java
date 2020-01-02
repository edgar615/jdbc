package com.github.edgar615.jdbc.codegen.gen;

import com.github.edgar615.jdbc.codegen.db.ParameterType;
import com.github.edgar615.jdbc.codegen.db.Table;

public class DomainKitGenerator implements Generator {

  private final CodegenOptions codegenOptions;

  private static final String tplFile = "tpl/domainKit.hbs";

  public DomainKitGenerator(CodegenOptions codegenOptions) {
    this.codegenOptions = codegenOptions;
  }

  public static DomainKitGenerator create(CodegenOptions codegenOptions) {
    return new DomainKitGenerator(codegenOptions);
  }

  @Override
  public void generate(Table table) {
    Codegen codegen = new Codegen(codegenOptions.getSrcFolderPath(), codegenOptions.getDomainPackage(), "Kit",
        tplFile);
    table.getColumns().stream()
        .filter(c -> !c.isIgnore())
        .map(c -> c.getParameterType())
        .forEach(t -> {
          if (t == ParameterType.DATE) {
            codegen.addImport("java.util.Date");
          }
          if (t == ParameterType.TIMESTAMP) {
            codegen.addImport("java.sql.Timestamp");
          }
          if (t == ParameterType.BIGDECIMAL) {
            codegen.addImport("java.math.BigDecimal");
          }
        });
    codegen.addImport("java.util.List");
    codegen.addImport("java.util.Map");
    codegen.addImport("com.google.common.collect.Lists");
    codegen.addImport("com.github.edgar615.entity.PersistentKit");
    codegen.genCode(table);
  }
}
