package com.bjtu.dao;

import com.bjtu.bean.UserInfoBean;

/**
 * Created by gimling on 17-4-21.
 */
public interface UserInfoDAO {

    UserInfoBean getUserInfoByUserId(Long userId);

    void saveUserInfoBean(UserInfoBean uib);

}
