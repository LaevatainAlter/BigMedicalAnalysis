package com.bjtu.dao.impl;

import com.bjtu.bean.UserBean;
import com.bjtu.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@Transactional
public class UserDAOImpl implements UserDAO {

    private final static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

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
    public Boolean saveUserBean(UserBean ub) {
        entityManager.persist(ub);
        return true;
    }

    @Override
    @Cacheable(cacheNames = "userCache",key = "#id")
    public UserBean findUserById(Long id) {
        UserBean ub = entityManager.find(UserBean.class,id);
        entityManager.detach(ub);
        return ub;
    }

    @Override
    @CacheEvict(cacheNames = "userCache",key = "#ub.id")
    public boolean update(UserBean ub) {
        if(ub.getUserInfoBean()!=null){
            if(ub.getUserInfoBean().getId()==null){
                entityManager.persist(ub.getUserInfoBean());
            }else{
                entityManager.merge(ub.getUserInfoBean());
            }
        }
        entityManager.merge(ub);
        return true;
    }
}
