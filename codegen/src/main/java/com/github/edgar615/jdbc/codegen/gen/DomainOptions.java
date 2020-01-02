package com.github.edgar615.jdbc.codegen.gen;

public class DomainOptions {

  private static final boolean DEFAULT_GEN_JPA = false;

  private boolean genJpa = DEFAULT_GEN_JPA;

  public boolean isGenJpa() {
    return genJpa;
  }

  public DomainOptions setGenJpa(boolean genJpa) {
    this.genJpa = genJpa;
    return this;
  }

}
