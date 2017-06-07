package com.bjtu.dao.impl;

import com.bjtu.bean.UploadRecordBean;
import com.bjtu.bean.UserBean;
import com.bjtu.dao.UploadFileDAO;
import com.bjtu.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by gimling on 17-6-4.
 */
@Repository
@Transactional
public class UploadFileDAOImpl implements UploadFileDAO {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserDAO userDAO;

    @Override
    public void save(UploadRecordBean urb) {
        if(urb.getUserBean()==null){
            throw new NullPointerException();
        }
        em.persist(urb);
    }

    @Override
    public void save(UploadRecordBean upb, Long uid) {
        UserBean ub = userDAO.findUserById(uid);
        upb.setUserBean(ub);
        em.persist(upb);
    }

    @Override
    public List getUploadRecords(Long uid) {
        UserBean ub = userDAO.findUserById(uid);
        ub = em.find(UserBean.class,ub.getId());
        return ub.getUploadRecords();
    }

    @Override
    public List getUploadRecords(Long uid, String query) {
        TypedQuery tq = em.createQuery("SELECT  urb FROM UploadRecordBean urb WHERE urb.originName like '%"+query+"%'",UploadRecordBean.class);
        return tq.getResultList();
    }

    @Override
    public UploadRecordBean getUploadRecordById(Long id) {
        return em.find(UploadRecordBean.class,id);
    }
}
