package com.github.edgar615.jdbc.codegen.gen;

import com.github.edgar615.jdbc.codegen.db.Table;

public class DomainRuleGenerator implements Generator {

  private final CodegenOptions codegenOptions;

  private static final String tplFile = "tpl/rule.hbs";

  private DomainRuleGenerator(CodegenOptions codegenOptions) {
    this.codegenOptions = codegenOptions;
  }

  public static DomainRuleGenerator create(CodegenOptions codegenOptions) {
    return new DomainRuleGenerator(codegenOptions);
  }

  @Override
  public void generate(Table table) {
    Codegen codegen = new Codegen(codegenOptions.getSrcFolderPath(),
        codegenOptions.getDomainPackage(), "Rule", tplFile);
    codegen.addImport("com.google.common.collect.ArrayListMultimap")
        .addImport("com.google.common.collect.Multimap")
        .addImport("com.github.edgar615.validation.rule.Rule");
    codegen.genCode(table);
  }
}
