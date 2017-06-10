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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by gimling on 17-6-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserInfoServiceTest {

    @Test
    public void saveUserInfo1()  {

    }

    @Autowired
    UserInfoService uis;

    @Autowired
    UserService us;

    UserBean ub1;

    UserBean ub2;


    @Before
    public void setUp() throws Exception {
        ub1 = new UserBean("1asdfasd","asdasd");
        ub1.setUserInfoBean(new UserInfoBean());
        us.updateUser(ub1);

        ub2 = new UserBean("1asdfasd","asdasd");
        us.updateUser(ub2);
    }

    @Test(expected = NullPointerException.class)
    public void getUserInfoByUID1() throws Exception {
        assertNotNull(uis.getUserInfoByUID(ub1.getId()));
    }

    @Test()
    public void getUserInfoByUID2() throws Exception {
        assertNull(uis.getUserInfoByUID(ub2.getId()));
    }

    @Test
    public void saveUserInfo() throws Exception {
        UserBean ub = us.findUserById(1L);
        assertNull(ub.getUserInfoBean());
        UserInfoBean uib = new UserInfoBean();
        uib.setUserPhone("123123");
        uib.setUserBean(ub);
        uis.saveUserInfo(1L, uib);
        ub.setUserInfoBean(uib);
        uib = us.findUserById(1L).getUserInfoBean();
        uib.setUserBirth(new Date());
        uis.saveUserInfo(1L, uib);
    }

}