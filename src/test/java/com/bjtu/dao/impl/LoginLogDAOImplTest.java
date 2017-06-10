package com.bjtu.dao.impl;

import com.bjtu.bean.LoginLogBean;
import com.bjtu.bean.UserBean;
import com.bjtu.config.SpringBoot;
import com.bjtu.dao.LoginLogDAO;
import com.bjtu.dao.UserDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by gimling on 17-6-10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LoginLogDAOImplTest {

    UserBean ub;

    @Autowired
    LoginLogDAO loginLogDAO;

    @Autowired
    UserDAO userDAO;

    LoginLogBean llb1 ;

    LoginLogBean llb2 ;
    @Before
    public void setUp() throws Exception {
        ub = new UserBean("2sadasd","asdasd12");
        userDAO.saveUserBean(ub);
        llb1 = new  LoginLogBean("192.168.67.1" ) ;
        llb1.setUserBean(ub);
        loginLogDAO.saveLoginLog(llb1);
        llb2 = new  LoginLogBean("192.168.67.2" ) ;
        llb2.setUserBean(ub);
        loginLogDAO.saveLoginLog(llb2);
    }

    @Test
    public void saveLoginLog1() throws Exception {
        LoginLogBean llb  = new LoginLogBean();
        llb.setRecordIP("127.0.0.1");
        llb.setUserBean(ub);
        loginLogDAO.saveLoginLog(llb);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveLoginLog2() {
        LoginLogBean llb  = new LoginLogBean();
        llb.setRecordIP("127.0.0.1");
        boolean result = loginLogDAO.saveLoginLog(llb);
        assertEquals(true,result);
    }

    @Test
    public void getLoginlogs() throws Exception {
        LoginLogBean[] logs = new LoginLogBean[]{llb2,llb1};
        assertArrayEquals(logs,loginLogDAO.getLoginlogs(ub.getId()).toArray());
    }

}