package com.bjtu.config;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.dao.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;


/**
 * Created by Gimling on 2017/4/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserDAO userRepository;

    private final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    @Before
    @Transactional
    public void insertRecord(){
        UserBean ub = new UserBean();
        ub.setUsername("343750470@qq.com");
        ub.setNickname("Gimling");ub.setNickname("Gimling");
        ub.setPassword(passwordEncoder.encodePassword("123456",null));
        UserInfoBean uib = new UserInfoBean();
        uib.setUserPhone("17801020520");
        uib.setUserHosipital("北京交通大学校医院");
        uib.setUserBirth(new Date());
        uib.setUserSex(false);
        ub.setUserInfoBean(uib);
        uib.setUserBean(ub);
        entityManager.persist(uib);
        entityManager.persist(ub);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void modify(){
       UserInfoBean uib = entityManager.find(UserInfoBean.class,1L);
       UserBean ub = entityManager.find(UserBean.class,1L);
    }

    @Test
    @Transactional
    public void read(){
        UserBean ub = entityManager.find(UserBean.class,1L);
        //ub.getUserInfoBean();
    }

    @After
    @Transactional
    public void commit(){
        entityManager.close();
    }

}