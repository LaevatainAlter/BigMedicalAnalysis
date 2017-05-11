package com.bjtu.service;

import com.bjtu.bean.LoginLog;
import com.bjtu.dao.LoginLogDAO;
import com.bjtu.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by gimling on 17-4-21.
 */
@Service
@Transactional
public class LoginLogService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginLogDAO loginLogDAO;

    @Autowired
    EntityManager entityManager;


    public List<LoginLog> getLoginLogs(Long userid){
        //UserBean ub = entityManager.find(UserBean.class,userid);
       // entityManager.createQuery("SELECT log FROM LoginLog log WHERE log.id")
        //return ub.getLoginLog();
        return loginLogDAO.getLoginlogs(userid);
    }

    public void log(LoginLog ll){
        loginLogDAO.saveLoginLog(ll);
    }

}
