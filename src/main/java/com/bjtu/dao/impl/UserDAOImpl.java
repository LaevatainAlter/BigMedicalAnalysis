package com.bjtu.dao.impl;

import com.bjtu.bean.UserBean;
import com.bjtu.dao.UserDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by gimling on 17-4-20.
 */
@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserBean findUserByName(String name) {
        Query query = entityManager.createQuery("select ub from UserBean ub where ub.username = :name or ub.nickname = :name",UserBean.class);
        query.setParameter("name",name);
        List<UserBean> rs = query.getResultList();
        if(rs.isEmpty()){
            return null;
        }else{
            return rs.get(0);
        }
    }

    @Override
    public UserBean findUserByUsername(String name) {
        Query query = entityManager.createQuery("select ub from UserBean ub where ub.username = :name",UserBean.class);
        query.setParameter("name",name);
        List<UserBean> rs = query.getResultList();
        if(rs.isEmpty()){
            return null;
        }else{
            return rs.get(0);
        }
    }

    @Override
    public UserBean findUserByNickname(String name) {
        Query query = entityManager.createQuery("select ub from UserBean ub where ub.nickname = :name",UserBean.class);
        query.setParameter("name",name);
        List<UserBean> rs = query.getResultList();
        if(rs.isEmpty()){
            return null;
        }else{
            return rs.get(0);
        }
    }

    @Override
    @Transactional
    public Boolean saveUserBean(UserBean ub) {
        entityManager.persist(ub.getUserInfoBean());
        entityManager.persist(ub);
        return true;
    }

    @Override
    public UserBean findUserById(Long id) {
        return entityManager.find(UserBean.class,id);
    }
}
