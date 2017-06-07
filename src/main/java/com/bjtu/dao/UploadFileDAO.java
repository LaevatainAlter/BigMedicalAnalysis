package com.bjtu.dao;

import com.bjtu.bean.UploadRecordBean;

import java.util.List;

/**
 * Created by gimling on 17-6-4.
 */
public interface UploadFileDAO {

    void save(UploadRecordBean urb);

    void save(UploadRecordBean upb, Long uid);

    List getUploadRecords(Long uid);

    List getUploadRecords(Long uid,String query);

    UploadRecordBean getUploadRecordById(Long id);
}
