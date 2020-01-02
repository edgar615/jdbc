package com.github.edgar615.jdbc.codegen.gen;

import com.github.edgar615.jdbc.codegen.db.ParameterType;
import com.github.edgar615.jdbc.codegen.db.Table;

public class DomainGenerator implements Generator {

  private final CodegenOptions codegenOptions;

  private final DomainOptions domainOptions;

  private static final String domainTplFile = "tpl/domain.hbs";

  private static final String jpaDomainTplFile = "tpl/jpaDomain.hbs";

  private DomainGenerator(CodegenOptions codegenOptions,
      DomainOptions domainOptions) {
    this.codegenOptions = codegenOptions;
    this.domainOptions = domainOptions;
  }

  public static DomainGenerator create(CodegenOptions codegenOptions,
      DomainOptions domainOptions) {
    return new DomainGenerator(codegenOptions, domainOptions);
  }

  @Override
  public void generate(Table table) {
    Codegen codegen = new Codegen(codegenOptions.getSrcFolderPath(),
        codegenOptions.getDomainPackage(), "",
        domainOptions.isGenJpa() ? jpaDomainTplFile : domainTplFile);
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
    codegen.addImport("com.google.common.base.MoreObjects");
    codegen.addImport("com.github.edgar615.entity.Persistent");
    if (domainOptions.isGenJpa()) {
      codegen.addImport("javax.persistence.Column");
      codegen.addImport("javax.persistence.Entity");
      codegen.addImport("javax.persistence.Id");
      codegen.addImport("javax.persistence.Table");
      boolean containsVersion = table.getColumns().stream()
          .filter(c -> !c.isIgnore())
          .anyMatch(c -> c.isVersion());
      if (containsVersion) {
        codegen.addImport("javax.persistence.Version");
      }
      if (table.getContainsVirtual()) {
        codegen.addImport("com.github.edgar615.jdbc.VirtualKey");
      }
    }

    codegen.genCode(table);
  }
}
