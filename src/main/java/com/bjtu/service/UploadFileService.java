package com.bjtu.service;

import com.bjtu.algorithm.ecg.algorithm.ECGAnlysis;
import com.bjtu.bean.UploadRecordBean;
import com.bjtu.dao.UploadFileDAO;
import com.bjtu.dao.UserDAO;
import com.bjtu.util.GlobalVariableHolder;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gimling on 17-6-4.
 */
@Service
public class UploadFileService {

    private final static Logger logger = LoggerFactory.getLogger(UploadFileService.class);

    public Map processFile(MultipartFile file, String webRoot) throws IOException {
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
                logger.error("文件创建创建失败，uid:{}", GlobalVariableHolder.getCurrentUserId());
                throw e;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(persistFile);
            os.write(file.getBytes());
            os.close();
        } catch (IOException e) {
            logger.error("无法打开输出流,uid:{}", GlobalVariableHolder.getCurrentUserId());
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
            UploadRecordBean urb = new UploadRecordBean();
            urb.setOriginName(file.getOriginalFilename());
            urb.setUuid(uuid);
            urb.setAnalysis(true);
            uploadFileDAO.save(urb);
            return json;
        } catch (IOException e) {
            logger.error("文件解析失败，uid:{}", GlobalVariableHolder.getCurrentUserId());
            throw e;
        } finally {
            return json;
        }
        //return "{\"rate\":{\"t\":{\"value\":1.52,\"prompt\":-1},\"n\":{\"value\":2,\"prompt\":0},\"tr\":{\"value\":203.33,\"prompt\":1},\"sr\":{\"value\":92.16,\"prompt\":1},\"ar\":{\"value\":212.28,\"prompt\":1},\"asd\":{\"value\":23.76,\"prompt\":0},\"ar_sr\":{\"value\":120.12,\"prompt\":1}},\"poincare\":{\"sd1\":{\"value\":23,\"prompt\":0},\"sd2\":{\"value\":14,\"prompt\":-1},\"sd1_sd2\":{\"value\":1.64,\"prompt\":1},\"ps\":{\"value\":346,\"prompt\":-1}},\"evaluate\":{\"chillsFever\":0,\"heartHiddenGod1\":2,\"heartHiddenGod2\":2,\"heartHiddenGod3\":0,\"heartBloodVeinFilling\":4},\"suggests\":[{\"result\":\"阳热状态\",\"symptom\":\"手脚发热、不怕冷怕热、穿得少盖得少、心烦、多汗\",\"recuperate\":\"1.银花上品冲调粉，5克/次，2次/日，温水冲服。\\n2.冷水浴或游泳。\\n3.少吃羊肉或辛辣食品\"},{\"result\":\"睡眠时间过短 睡眠轻浅\",\"symptom\":\"感冒、多梦、夜尿多、性欲低、食欲差、脾气爆、皮肤干\",\"recuperate\":\"1.酸枣上品冲调粉，10克/次，2次/日，温水冲服。\\n2.不吃过饱。\\n3.不饮用咖啡、茶叶\"},{\"result\":\"过度兴奋\",\"symptom\":\"多语、多动、性格外向、甚至躁狂不安\",\"recuperate\":\"1.百合上品冲调粉，10克/次，2次/日，温水冲服。\\n2.放慢生活节奏。\\n3.打太极、练瑜伽、听舒缓音乐\"},{\"result\":\"心脏的潜力很差\",\"symptom\":\"胸闷憋气、胸痛、心慌、下肢浮肿\",\"recuperate\":\"1.到医院就诊。\\n2.严禁兴奋、过饱、用例排便\"}]}";
    }

    public List getUploadList(Long uid, String query) {
        if (query == null || query.isEmpty()) {
            return uploadFileDAO.getUploadRecords(uid);
        } else {
            return getUploadList(uid,query);
        }
    }

    public Map getFileById(Long id,String webRoot) throws IOException {
        Map json = new LinkedHashMap();
        UploadRecordBean urb = uploadFileDAO.getUploadRecordById(id);
        if(urb==null){
            json.put("msg","文件不存在");
            return json;
        }
        ObjectMapper om = new ObjectMapper();
        String root = webRoot + "upload" + File.separator + "file" + File.separator;
        File userDir = new File(root);
        if (!userDir.exists()) {
            userDir.mkdir();
        }
        String uuid = urb.getUuid();
        File dataFile = new File(userDir + File.separator + uuid + ".data" );
        return om.readValue(dataFile,Map.class);
    }

    @Autowired
    UploadFileDAO uploadFileDAO;

    @Autowired
    UserDAO userDAO;
}
