package com.github.edgar615.jdbc.codegen.gen;

import com.github.edgar615.jdbc.codegen.db.DBFetcher;
import com.github.edgar615.jdbc.codegen.db.Table;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2017/5/17.
 *
 * @author Edgar  Date 2017/5/17
 */
public class Generators {

  private static final Logger LOGGER = LoggerFactory.getLogger(Generators.class);

  private final CodegenOptions options;

  private final List<Generator> generators = new ArrayList<>();

  private Generators(CodegenOptions options) {
    this.options = options;
  }

  public static Generators create(CodegenOptions options) {
    return new Generators(options);
  }

  public Generators addGenerator(Generator generator) {
    this.generators.add(generator);
    return this;
  }

  public void generate() {
    List<Table> tables = new DBFetcher(options).fetchTablesFromDb();
    for (Generator generator : generators) {
      tables.stream()
          .filter(t -> !t.isIgnore())
          .forEach(t -> generator.generate(t));
    }

  }


}
