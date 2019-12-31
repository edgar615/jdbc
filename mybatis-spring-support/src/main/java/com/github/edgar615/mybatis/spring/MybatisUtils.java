package com.github.edgar615.mybatis.spring;

import com.github.edgar615.util.base.StringUtils;
import java.util.List;

public class MybatisUtils {

  private static final String REVERSE_KEY = "-";

  public static String orderBy(String orderBy) {
    if (orderBy.startsWith(REVERSE_KEY)) {
      return StringUtils.underscoreName(orderBy.substring(1)) + " desc ";
    } else {
      return StringUtils.underscoreName(orderBy);
    }
  }

//  public static List<String> fields()
}
