package com.bjtu.service;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.dao.UserDAO;
import com.bjtu.dao.UserInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gimling on 17-4-21.
 */
@Service
@Transactional
public class UserInfoService {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;


    public UserInfoBean getUserInfoByUID(Long uid){
        UserBean ub = userDAO.findUserById(uid);
        UserInfoBean uib =ub.getUserInfoBean();
        if(uib==null){
            uib = new UserInfoBean();
            uib.setUserBean(ub);
            ub.setUserInfoBean(uib);
        }
        return  uib;
    }

    public void saveUserInfo(Long uid,UserInfoBean uib){
        userDAO.findUserById(uid).setUserInfoBean(uib);
        userInfoDAO.saveUserInfoBean(uib);
    }
}
