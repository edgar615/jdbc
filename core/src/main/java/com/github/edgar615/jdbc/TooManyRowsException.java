package com.github.edgar615.jdbc;

public class TooManyRowsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    TooManyRowsException(int count) {
        super("Expected single result, found " + count + " rows");
    }

}