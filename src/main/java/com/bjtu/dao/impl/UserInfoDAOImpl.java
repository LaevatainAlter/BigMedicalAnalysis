package com.bjtu.dao.impl;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.dao.UserInfoDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by gimling on 17-4-21.
 */
@Repository
public class UserInfoDAOImpl implements UserInfoDAO {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserInfoBean getUserInfoByUserId(Long userId) {
        UserBean ub  = entityManager.find(UserBean.class,userId);
        return ub.getUserInfoBean();
    }

    @Override
    @Transactional
    public void saveUserInfoBean(UserInfoBean uib) {
         entityManager.merge(uib);
    }
}
