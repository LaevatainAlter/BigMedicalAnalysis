package com.bjtu.dao.impl;

import com.bjtu.bean.UploadRecordBean;
import com.bjtu.config.SpringBoot;
import com.bjtu.dao.UploadFileDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by gimling on 17-6-4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadFileDAOImplTest {

    @Test
    public void getUploadRecords() throws Exception {
        List list =     uploadFileDAO.getUploadRecords(1L,"123123123");
        System.out.println(list);
    }

    @Autowired
    UploadFileDAO uploadFileDAO;

    @Test
    public void save() throws Exception {
        uploadFileDAO.save(new UploadRecordBean(),1L);
    }


}