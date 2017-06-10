package com.bjtu.dao.impl;

import com.bjtu.bean.LoginLogBean;
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
    public boolean saveLoginLog(LoginLogBean ll) {
        entityManager.persist(ll);
        return true;
    }

    @Override
    public List<LoginLogBean> getLoginlogs(Long uid) {
        Query query = entityManager.createQuery("SELECT log FROM LoginLogBean log where log.userBean.id = :uid ORDER BY log.id DESC", LoginLogBean.class);
        query.setParameter("uid", uid);
        query.setMaxResults(10);
        List<LoginLogBean> rs = query.getResultList();
        if (rs.isEmpty()) {
            return null;
        } else {
            return rs;
        }
    }


}
