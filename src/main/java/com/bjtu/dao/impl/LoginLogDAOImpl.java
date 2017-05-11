package com.bjtu.dao.impl;

import com.bjtu.bean.LoginLog;
import com.bjtu.dao.LoginLogDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by gimling on 17-4-21.
 */
@Repository
public class LoginLogDAOImpl implements LoginLogDAO {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void saveLoginLog(LoginLog ll) {
        entityManager.persist(ll);
    }

    @Override
    public List<LoginLog> getLoginlogs(Long uid) {
        Query query = entityManager.createQuery("SELECT log FROM LoginLog log where log.userBean.id = :uid ORDER BY log.id DESC",LoginLog.class);
        query.setParameter("uid",uid);
        query.setMaxResults(10);
        List<LoginLog> rs = query.getResultList();
        if(rs.isEmpty()){
            return null;
        }else{
            return rs;
        }
    }


}
