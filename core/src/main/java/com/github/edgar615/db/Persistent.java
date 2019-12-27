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

import java.io.Serializable;

/**
 * 实体类的接口.
 *
 * @param <ID> the type of the identifier
 */
public interface Persistent<ID> extends Serializable {

  /**
   * Returns the id of the entity.
   *
   * @return the id
   */
  ID id();

  static <ID> Persistent create(Class<? extends Persistent<ID>> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  void setId(ID id);

  /**
   * 设置自增主键
   */
  void setGeneratedKey(Number key);
}
