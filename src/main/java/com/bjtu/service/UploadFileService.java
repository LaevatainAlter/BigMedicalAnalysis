package com.bjtu.service;

import com.bjtu.algorithm.ecg.algorithm.ECGAnlysis;
import com.bjtu.bean.UploadRecordBean;
import com.bjtu.dao.UploadFileDAO;
import com.bjtu.dao.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.bjtu.service.JsonService.fromJson;

/**
 * Created by gimling on 17-6-4.
 */
@Service
public class UploadFileService {

    private final static Logger logger = LoggerFactory.getLogger(UploadFileService.class);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");

    public Map processFile(MultipartFile file, String webRoot, String patientName, String patientTime, Long uid) throws IOException, ParseException {
        ObjectMapper om = new ObjectMapper();
        String root = webRoot + "upload" + File.separator + "file" + File.separator;
        File userDir = new File(root);
        if (!userDir.exists()) {
            userDir.mkdir();
        }
        String uuid = UUID.randomUUID().toString();
        File persistFile = new File(userDir + File.separator + uuid + "." + file.getOriginalFilename().split("\\.")[1]);
        if (!persistFile.exists()) {
            try {
                persistFile.createNewFile();
            } catch (IOException e) {
                logger.error("文件创建创建失败，uid:{}", uid);
                throw e;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(persistFile);
            os.write(file.getBytes());
            os.close();
        } catch (IOException e) {
            logger.error("无法打开输出流,uid:{}", uid);
            throw e;
        }
        Map json = new LinkedHashMap();
        try {
            ECGAnlysis ecg = new ECGAnlysis(persistFile);        // 输入
            json.put("lineChart", ecg.getHeartBeatPerSecond());
            json.put("scatterPlot", ecg.getRRDistance());
            json.put("outcome", ecg.getNumerics());
            File dataFile = new File(userDir + File.separator + uuid + ".data");
            om.writeValue(dataFile,json);
            UploadRecordBean urb = new UploadRecordBean(file.getOriginalFilename(),uuid,userDAO.findUserById(uid));
            urb.setAnalysis(true);
            urb.setPatientName(patientName);
            urb.setPatientDate(simpleDateFormat.parse(patientTime));
            uploadFileDAO.save(urb);
            return json;
        } catch (IOException e) {
            logger.error("文件解析失败，uid:{}", uid);
            throw e;
        } finally {
            return json;
        }
    }


    public List getUploadList(Long uid, String query) {
        if (query == null || query.isEmpty()) {
            return uploadFileDAO.getUploadRecords(uid);
        } else {
            return getUploadList(uid,query);
        }
    }

    public Map getFileById(Long id,String webRoot,Long uid) throws IOException {
        Map json = new LinkedHashMap();
        UploadRecordBean urb = uploadFileDAO.getUploadRecordById(id);
        if(urb==null||urb.getUserBean()==null&&urb.getUserBean().getId()!=uid){
            json.put("msg","文件不存在");
            return json;
        }
        String root = webRoot + "upload" + File.separator + "file" + File.separator;
        File userDir = new File(root);
        if (!userDir.exists()) {
            userDir.mkdir();
        }
        String uuid = urb.getUuid();
        File dataFile = new File(userDir + File.separator + uuid + ".data" );
        return fromJson(dataFile,Map.class);
    }

    @Autowired
    UploadFileDAO uploadFileDAO;

    @Autowired
    UserDAO userDAO;
}
