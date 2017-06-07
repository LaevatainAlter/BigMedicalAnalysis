package com.bjtu.ctrl;

import com.bjtu.bean.UploadRecordBean;
import com.bjtu.service.UploadFileService;
import com.bjtu.util.GlobalVariableHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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
    public Map getHistoryList(@RequestParam(value = "query", required = false) String query) {
        Map json = new HashMap();
        List historyList = uploadFileService.getUploadList(GlobalVariableHolder.getCurrentUserId(), query);
        List jsonList = new ArrayList();
        Map history = null;
        for (Object o : historyList) {
            UploadRecordBean urb = (UploadRecordBean) o;
            history = new LinkedHashMap();
            history.put("historyTime", urb.getPatientDate() != null ? dateTimeFormatter.format(urb.getPatientDate()) : "");
            history.put("historyName", urb.getPatientName() != null ? urb.getPatientName() : "");
            history.put("historyId", urb.getId());
            jsonList.add(history);
        }
        json.put("historyList", jsonList);
        return json;
    }


    /**
     * 处理文件上传
     *
     * @param file    上传的文件
     * @param request
     * @return 返回处理结果的json, 包含散点图数据，折线图，及数据分析的结果
     * @throws IOException
     */
    @PostMapping(path = "/toUploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map uploadFile(@RequestParam(value = "file", required = false) MultipartFile file,
                          @RequestParam(value = "patientName", required = false, defaultValue = "") String patientName,
                          @RequestParam(value = "patientDate", required = false, defaultValue = "") String patientDate,
                          HttpServletRequest request) throws IOException, ParseException {
        return uploadFileService.processFile(file, request.getRealPath("/"), patientName, patientDate, GlobalVariableHolder.getCurrentUserId());
    }

    @PostMapping(path = "/getRecordDetail")
    @ResponseBody
    public Map getRecordDetail(@RequestBody Map requestJson, HttpServletRequest request) throws IOException {
        Map json = new LinkedHashMap();
        if (!requestJson.containsKey("historyId")) {
            json.put("msg", "参数错误，必须包含参数id");
            return json;
        }
        Long id = ((Integer) requestJson.get("historyId")).longValue();
        return uploadFileService.getFileById(id, request.getRealPath("/"), GlobalVariableHolder.getCurrentUserId());
    }


    @Autowired
    private UploadFileService uploadFileService;

}
