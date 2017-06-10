package com.bjtu.dao.impl;

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
@Transactional
public class UserInfoDAOImpl implements UserInfoDAO {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void saveUserInfoBean(UserInfoBean uib) {
        if(uib.getId()==null){
            entityManager.persist(uib);
        }else{
            entityManager.merge(uib);
        }
    }
}
