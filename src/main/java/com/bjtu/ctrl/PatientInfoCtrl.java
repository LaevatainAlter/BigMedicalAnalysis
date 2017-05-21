package com.bjtu.ctrl;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @PostMapping(path = "/toUploadFile",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String uploadFile(){
        return "{\n" +
                "  rate: {//心率变化的评估,value是值，prompt：1是高，0是正好，-1是低\n" +
                "    t: {value: 1.52, prompt: -1},//睡眠累计时间\n" +
                "    n: {value: 2.00, prompt: 0},//睡眠累计时间\n" +
                "    tr: {value: 203.33, prompt: 1},//24小时平均心率\n" +
                "    sr: {value: 92.16, prompt: 1},//睡眠期间平均心率\n" +
                "    ar: {value: 212.28, prompt: 1},//清醒期间平均心率\n" +
                "    asd: {value: 23.76, prompt: 0},//清醒期间心率标准差\n" +
                "    ar_sr: {value: 120.12, prompt: 1}//清醒和睡眠期间的心率差\n" +
                "  },\n" +
                "  poincare: {//散点图的评估，value是值，prompt：1是高，0是正好，-1是低\n" +
                "    sd1: {value: 23.00, prompt: 0},//短轴\n" +
                "    sd2: {value: 14.00, prompt: -1},//长轴\n" +
                "    sd1_sd2: {value: 1.64, prompt: 1},//SD1/SD2\n" +
                "    ps: {value: 346.00, prompt: -1}//面积\n" +
                "  },\n" +
                "  evaluate: {\n" +
                "    chillsFever: 0,//寒热状态评估，从0开始计时：阳热状态，均衡状态，阴寒状态\n" +
                "    heartHiddenGod1: 2,//中医心藏藏神功能评估，从0开始计时:睡眠时间过长，睡眠时间适中，睡眠时间过短\n" +
                "    heartHiddenGod2: 2,//中医心藏藏神功能评估，从0开始计时:睡眠深沉，睡眠正常，睡眠轻浅，多梦易睡\n" +
                "    heartHiddenGod3: 0,//中医心藏藏神功能评估，从0开始计时:过度兴奋，精力充沛，精神一般，精神不振\n" +
                "    heartBloodVeinFilling:4//中医心藏藏血充脉功能评估，从0开始计时:优秀，良好，稍差，较差，很差\n" +
                "  },\n" +
                "  suggests: [//常见症状和调理建议\n" +
                "    {result: '阳热状态', symptom: '手脚发热、不怕冷怕热、穿得少盖得少、心烦、多汗', recuperate: '1.银花上品冲调粉，5克/次，2次/日，温水冲服。\\n2.冷水浴或游泳。\\n3.少吃羊肉或辛辣食品'},\n" +
                "    {result: '睡眠时间过短 睡眠轻浅', symptom: '感冒、多梦、夜尿多、性欲低、食欲差、脾气爆、皮肤干', recuperate: '1.酸枣上品冲调粉，10克/次，2次/日，温水冲服。\\n2.不吃过饱。\\n3.不饮用咖啡、茶叶'},\n" +
                "    {result: '过度兴奋', symptom: '多语、多动、性格外向、甚至躁狂不安', recuperate: '1.百合上品冲调粉，10克/次，2次/日，温水冲服。\\n2.放慢生活节奏。\\n3.打太极、练瑜伽、听舒缓音乐'},\n" +
                "    {result: '心脏的潜力很差', symptom: '胸闷憋气、胸痛、心慌、下肢浮肿', recuperate: '1.到医院就诊。\\n2.严禁兴奋、过饱、用例排便'}\n" +
                "  ]\n" +
                "}";
    }

}
