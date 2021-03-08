package com.github.edgar615.jdbc.spring;

import com.github.edgar615.jdbc.Jdbc;
import com.github.edgar615.jdbc.spring.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"application.yml"})
public class JdbcTest {

  @Autowired
  private Jdbc jdbc;

  @Test
  public void testFindFirst() {
    User device = jdbc.findById(User.class, 1L);
    System.out.println(device);

    device = jdbc.findById(User.class, 1L);
    System.out.println(device);

    device = new User();
    device.setNickname("1");
    jdbc.updateById(device, 1L);

    jdbc.findById(User.class, 1L);
  }
}
