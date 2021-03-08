package com.github.edgar615.jdbc.spring;

import com.github.edgar615.jdbc.Jdbc;
import com.github.edgar615.jdbc.spring.entity.User;
import com.github.edgar615.util.search.Example;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CachedJdbcTest {

  @Autowired
  private Jdbc jdbc;

  @Test
  public void testFindFirst() {
    User user = jdbc.findById(User.class, 3L);
    System.out.println(user);

    user = jdbc.findById(User.class, 3L);
    System.out.println(user);

    user = new User();
    user.setNickname("3");
    jdbc.updateById(user, 3L);

    jdbc.findById(User.class, 3L);
  }

  @Test
  public void testField() {
    User user = jdbc.findById(User.class, 3L);
    System.out.println(user);

    user = jdbc.findById(User.class, 3L, Lists.newArrayList("userId"));
    System.out.println(user);

    user = new User();
    user.setNickname("3");
    Example example = Example.create()
        .equalsTo("userId", 1L);
    jdbc.updateByExample(user, example);

    jdbc.findById(User.class, 3L);
  }

  @Test
  public void testInsert() {
    User user = jdbc.findById(User.class, 1L);
    System.out.println(user);

    user = jdbc.findById(User.class, 1L);
    System.out.println(user);

    user = new User();
    user.setUsername("0");
    user.setNickname("000000000000000");
    jdbc.insert(user);

    user = jdbc.findById(User.class, 1L);
    System.out.println(user);

    user.setUserId(2L);
    jdbc.batchInsert(Lists.newArrayList(user));;
    user = jdbc.findById(User.class, 2L);
    System.out.println(user);
    user = jdbc.findById(User.class, 2L);
    System.out.println(user);

  }

  @Test
  public void testDelete() {
    User user = jdbc.findById(User.class, 1L);
    System.out.println(user);

    user = jdbc.findById(User.class, 1L);
    System.out.println(user);

    jdbc.deleteById(User.class, 1L);

    user = jdbc.findById(User.class, 1L);
    System.out.println(user);
  }

}
