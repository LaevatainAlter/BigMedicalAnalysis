package com.bjtu.service;

import com.bjtu.config.SpringBoot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by gimling on 17-6-7.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    @Autowired
    UserService us;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    //@Transactional
    public void processRegister() throws Exception {
        Map map = new LinkedHashMap();
        map.put("username","17801020520@163.com");
        map.put("password","123456");
        map.put("repeat-password","123456");
        map.put("code","1234");
        Map expect = new LinkedHashMap();
        expect.put("status",true);

        assertEquals(expect,us.processRegister(map));
        System.out.println('s');
    }

    @Test
    public void registerValidate1() throws Exception {
        Map map = new LinkedHashMap();
        map.put("username","黄锦龙");
        Map expect = new LinkedHashMap();
        expect.put("status", false);
        expect.put("errorMsg", "密码不能为空");
        expect.put("errorField", "password");

        assertEquals(expect,us.registerValidate(map));
    }

    @Test
    public void registerValidate2() throws Exception {
        Map map = new LinkedHashMap();
        map.put("username1","黄锦龙");
        Map expect = new LinkedHashMap();
        expect.put("status", false);
        expect.put("errorMsg", "用户名不能为空");
        expect.put("errorField", "username");

        assertEquals(expect,us.registerValidate(map));
    }

    @Test
    public void registerValidate3() throws Exception {
        Map map = new LinkedHashMap();
        map.put("username","黄锦龙");
        map.put("password","123456");
        map.put("repeat-password","123456");
        map.put("code","1234");
        Map expect = new LinkedHashMap();
        expect.put("status", false);
        expect.put("errorMsg", "验证码错误");
        expect.put("errorField", "code");

        assertEquals(expect,us.registerValidate(map));
    }

    @Test
    public void findUserById() throws Exception {
    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void cleanUserBeanCache() throws Exception {
    }

    @Test
    public void getCurrentUserId() throws Exception {
    }

}