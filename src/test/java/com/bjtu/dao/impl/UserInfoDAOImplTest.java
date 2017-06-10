package com.bjtu.dao.impl;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.config.SpringBoot;
import com.bjtu.dao.UserDAO;
import com.bjtu.dao.UserInfoDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by gimling on 17-6-10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserInfoDAOImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO ud;

    UserBean ub1;

    UserInfoBean uib1;

    @Before
    public void setUp(){
        uib1 = new UserInfoBean();
        uib1.setUserSex(false);
        ub1 = new UserBean("ttesss","123123");
        ub1.setUserInfoBean(uib1);
        uib1.setUserBean(ub1);
        em.persist(ub1);
        userInfoDAO.saveUserInfoBean(uib1);
    }

    @Test
    public void saveUserInfoBean() throws Exception {
        UserBean ub2 = ud.findUserById(ub1.getId());
        UserInfoBean uib2  = ub2.getUserInfoBean();
        uib2.setUserSex(true);
        uib2.setUserPhone("17801020521");
        uib2.setUserHosipital("某医院");
        userInfoDAO.saveUserInfoBean(uib2);
        UserBean ub3 = ud.findUserById(ub1.getId());
        assertEquals(true,ub3.getUserInfoBean().getUserSex());
        assertEquals("17801020521",ub3.getUserInfoBean().getUserPhone());
        assertEquals("某医院",ub3.getUserInfoBean().getUserHospital());
    }

}