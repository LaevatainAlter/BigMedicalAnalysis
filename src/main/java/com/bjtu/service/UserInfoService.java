package com.bjtu.service;

import com.bjtu.bean.UserInfoBean;
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

    public UserInfoBean getUserInfoByUID(Long uid){
        return userInfoDAO.getUserInfoByUserId(uid);
    }

    public void saveUserInfo(UserInfoBean uib){
        userInfoDAO.saveUserInfoBean(uib);
    }
}
