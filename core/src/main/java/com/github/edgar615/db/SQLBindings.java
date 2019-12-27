/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.edgar615.db;

import java.util.Collections;
import java.util.List;

/**
 * SQL.
 *
 * @author Edgar
 */
public class SQLBindings {

  private final String sql;

  private final List<Object> bindings;

  private SQLBindings(String sql, List<Object> bindings) {
    this.sql = sql;
    //ImmutableList不能包含null，改为使用JDK默认的不可修改集合
    this.bindings = Collections.unmodifiableList(bindings);
  }

  public static SQLBindings create(String sql, List<Object> bindings) {
    return new SQLBindings(sql, bindings);
  }

  public String sql() {
    return sql;
  }

  public List<Object> bindings() {
    return bindings;
  }

}
