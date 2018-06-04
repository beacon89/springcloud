package com.kayak;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) /*添加SpringJUnit支持，引入Spring-Test框架*/
@SpringBootTest(classes = SystemApplication.class) /*指定Springboot启动类启动*/
public class TestMysql {

	@Autowired
    private DataSource dataSource;

    @Test
    public void test() throws SQLException {
        System.out.println(dataSource);
        System.out.println(dataSource.getConnection());
    }


}
