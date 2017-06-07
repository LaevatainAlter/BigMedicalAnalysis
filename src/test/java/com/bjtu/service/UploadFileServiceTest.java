package com.bjtu.service;

import com.bjtu.bean.UserBean;
import com.bjtu.config.SpringBoot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by gimling on 17-6-4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadFileServiceTest {

    @Autowired
    UploadFileService ufs;

    @Autowired
    EntityManager em;

 /*   @Test
    public void getFileById() throws Exception {
        Map json = ufs.getFileById(2L,"/home/gimling/Documents/IdeaProjects/Medical/target/Medical/");
        System.out.println(json);
    }*/

 @Test
 @Transactional
    public void test(){
     UserBean ub = em.find(UserBean.class,1L);
     em.detach(ub);
     try {
         em.merge(ub);
     } catch (Exception e) {
         e.printStackTrace();
     }
     List list = ub.getUploadRecords();
     System.out.println(list);
 }

}