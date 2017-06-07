package com.bjtu.service;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.config.SpringBoot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Date;

import static org.junit.Assert.assertNull;

/**
 * Created by gimling on 17-6-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoServiceTest {
    @Test
    public void saveUserInfo1() throws Exception {
    }

    @Autowired
    UserInfoService uis;

    @Autowired
    UserService us;


    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void getUserInfoByUID1() throws Exception {
        uis.getUserInfoByUID(100L);
    }

    @Test()
    public void getUserInfoByUID2() throws Exception {
        assertNull(uis.getUserInfoByUID(1L));
    }

    @Test
    @Transactional
    public void saveUserInfo() throws Exception {
        UserBean ub = us.findUserById(1L);
        assertNull(ub.getUserInfoBean());
        UserInfoBean uib = new UserInfoBean();
        uib.setUserPhone("123123");
        uib.setUserBean(ub);
        uis.saveUserInfo(1L,uib);
        ub.setUserInfoBean(uib);
        uib = us.findUserById(1L).getUserInfoBean();
        uib.setUserBirth(new Date());
        uis.saveUserInfo(1L,uib);
    }

}