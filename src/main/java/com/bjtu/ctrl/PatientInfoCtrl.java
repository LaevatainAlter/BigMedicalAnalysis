package com.bjtu.ctrl;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gimling on 17-5-21.
 */
@Controller
public class PatientInfoCtrl {

    private final DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/toGetHistoryList")
    @ResponseBody
    public Map getHistoryList(){
        Map json = new HashMap();
        List<Map<String,String>> historyList = new ArrayList<>();
        Map history;
        for(int i=0;i<10;i++){
            history = new LinkedHashMap();
            history.put("historyTime",dateTimeFormatter.format(new Date()));
            history.put("historyName","第"+i+"个名字");
            historyList.add(history);
        }
        json.put("historyList",historyList);
        return json;
    }


    @PostMapping(path = "/toUploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String uploadFile(@RequestParam("file") Object o) throws IOException {
       return "{\"rate\":{\"t\":{\"value\":1.52,\"prompt\":-1},\"n\":{\"value\":2,\"prompt\":0},\"tr\":{\"value\":203.33,\"prompt\":1},\"sr\":{\"value\":92.16,\"prompt\":1},\"ar\":{\"value\":212.28,\"prompt\":1},\"asd\":{\"value\":23.76,\"prompt\":0},\"ar_sr\":{\"value\":120.12,\"prompt\":1}},\"poincare\":{\"sd1\":{\"value\":23,\"prompt\":0},\"sd2\":{\"value\":14,\"prompt\":-1},\"sd1_sd2\":{\"value\":1.64,\"prompt\":1},\"ps\":{\"value\":346,\"prompt\":-1}},\"evaluate\":{\"chillsFever\":0,\"heartHiddenGod1\":2,\"heartHiddenGod2\":2,\"heartHiddenGod3\":0,\"heartBloodVeinFilling\":4},\"suggests\":[{\"result\":\"阳热状态\",\"symptom\":\"手脚发热、不怕冷怕热、穿得少盖得少、心烦、多汗\",\"recuperate\":\"1.银花上品冲调粉，5克/次，2次/日，温水冲服。\\n2.冷水浴或游泳。\\n3.少吃羊肉或辛辣食品\"},{\"result\":\"睡眠时间过短 睡眠轻浅\",\"symptom\":\"感冒、多梦、夜尿多、性欲低、食欲差、脾气爆、皮肤干\",\"recuperate\":\"1.酸枣上品冲调粉，10克/次，2次/日，温水冲服。\\n2.不吃过饱。\\n3.不饮用咖啡、茶叶\"},{\"result\":\"过度兴奋\",\"symptom\":\"多语、多动、性格外向、甚至躁狂不安\",\"recuperate\":\"1.百合上品冲调粉，10克/次，2次/日，温水冲服。\\n2.放慢生活节奏。\\n3.打太极、练瑜伽、听舒缓音乐\"},{\"result\":\"心脏的潜力很差\",\"symptom\":\"胸闷憋气、胸痛、心慌、下肢浮肿\",\"recuperate\":\"1.到医院就诊。\\n2.严禁兴奋、过饱、用例排便\"}]}";
    }


}
