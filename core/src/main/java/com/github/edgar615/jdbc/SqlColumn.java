package com.github.edgar615.jdbc;

public class SqlColumn {
    private final String dbName;

    private final String javaName;

    private SqlColumn(String dbName, String javaName) {
        this.dbName = dbName;
        this.javaName = javaName;
    }

    public static SqlColumn create(String dbName, String javaName) {
        return new SqlColumn(dbName, javaName);
    }

    public String dbName() {
        return dbName;
    }

    public String javaName() {
        return javaName;
    }
}
