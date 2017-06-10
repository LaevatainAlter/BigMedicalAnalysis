package com.bjtu.dao.impl;

import com.bjtu.bean.UserBean;
import com.bjtu.config.SpringBoot;
import com.bjtu.dao.UserDAO;
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
 * Created by gimling on 17-6-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserDAOImplTest {


    @Before
    public void setUp(){
        ub1 = new UserBean("17801020520@163.com","hjl19960504");
        ub1.setNickname("呵呵哈哈哈或或或");
        em.persist(ub1);
    }

    @Autowired
    UserDAO ud;

    @Autowired
    EntityManager em;

    private UserBean ub1;

    @Test
    public void findUserById() throws Exception {
        UserBean ub2= ud.findUserById(ub1.getId());

        assertEquals(ub1.getId(),ub2.getId());
        assertEquals(ub1.getUsername(),ub2.getUsername());
        assertEquals(ub1.getPassword(),ub2.getPassword());
        assertEquals(ub1.getNickname(),ub2.getNickname());
    }

    @Test
    public void findUserByName() throws Exception {
        UserBean ub2 = ud.findUserByName(ub1.getUsername());

        assertEquals(ub1.getId(),ub2.getId());
        assertEquals(ub1.getUsername(),ub2.getUsername());
        assertEquals(ub1.getPassword(),ub2.getPassword());
        assertEquals(ub1.getNickname(),ub2.getNickname());

        ub2 = ud.findUserByName(ub1.getNickname());

        assertEquals(ub1.getId(),ub2.getId());
        assertEquals(ub1.getUsername(),ub2.getUsername());
        assertEquals(ub1.getPassword(),ub2.getPassword());
        assertEquals(ub1.getNickname(),ub2.getNickname());
    }

    @Test
    public void findUserByUsername() throws Exception {
        UserBean ub2 = ud.findUserByName(ub1.getUsername());

        assertEquals(ub1.getId(),ub2.getId());
        assertEquals(ub1.getUsername(),ub2.getUsername());
        assertEquals(ub1.getPassword(),ub2.getPassword());
        assertEquals(ub1.getNickname(),ub2.getNickname());
    }

    @Test
    public void findUserByNickname() throws Exception {
        UserBean ub2 = ud.findUserByName(ub1.getNickname());

        assertEquals(ub1.getId(),ub2.getId());
        assertEquals(ub1.getUsername(),ub2.getUsername());
        assertEquals(ub1.getPassword(),ub2.getPassword());
        assertEquals(ub1.getNickname(),ub2.getNickname());
    }

    @Test
    public void saveUserBean() throws Exception {
        UserBean ub2 = new UserBean("Void","asdasdasd");
        ub2.setNickname("213123");
        assertEquals(true,ud.saveUserBean(ub2));
    }

    @Test
    public void update() throws Exception {
        UserBean ub2 = ud.findUserById(ub1.getId());
        String expect = "this has been update";
        ub2.setNickname(expect);
        ud.update(ub2);
        ub2 = ud.findUserById(ub1.getId());
        assertEquals(expect,ub2.getNickname());
    }

}