package com.github.edgar615.jdbc.codegen.gen;

import com.github.edgar615.jdbc.codegen.db.Table;

public interface Generator {

  void generate(Table table);
}
