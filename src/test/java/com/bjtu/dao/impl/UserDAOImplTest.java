package com.bjtu.dao.impl;

import com.bjtu.bean.UserBean;
import com.bjtu.config.SpringBoot;
import com.bjtu.dao.UserDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by gimling on 17-6-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDAOImplTest {
    @Autowired
    UserDAO ud;

    @Test
    public void findUserById() throws Exception {
        UserBean ub = ud.findUserById(1L);
        ub.setUsername("123123");
        assertEquals(ub.getUsername(),ud.findUserById(1L).getUsername());
    }

}