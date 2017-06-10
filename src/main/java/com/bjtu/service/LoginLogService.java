package com.bjtu.service;

import com.bjtu.bean.LoginLogBean;
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


    public List<LoginLogBean> getLoginLogs(Long userid){
        return loginLogDAO.getLoginlogs(userid);
    }

    public void log(LoginLogBean ll){
        loginLogDAO.saveLoginLog(ll);
    }

}
