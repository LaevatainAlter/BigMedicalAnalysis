package com.bjtu.ctrl;

import com.bjtu.bean.LoginLog;
import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.service.LoginLogService;
import com.bjtu.service.UserInfoService;
import com.bjtu.service.UserService;
import com.bjtu.util.GlobalVariableHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gimling on 17-4-21.
 */

/**
 * 用户信息控制器
 * 要访问该控制器的方法要拥有ROLE_USER权限
 */
@PreAuthorize("hasRole('ROLE_USER')")
@Controller
public class UserInfoCtrl {

    /**
     * @return 包含用户信息的JSON字符串, 其格式如下:
     * {"id":’123456‘,
     * "userSex":true,
     * "userPhone":"17801020521",
     * "userBirth":"1907-01-01",
     * "userHospital":null,
     * "userRegTime":"2017-04-22",
     * "userName":"用户名",
     * "userEmail":"example@qq.com"}
     * @throws IOException 字符串生成失败
     */
    @GetMapping("/toGetPerInfo")
    @ResponseBody
    public Map getPerInfo() throws IOException {
        UserInfoBean uib = userInfoService.getUserInfoByUID(GlobalVariableHolder.getCurrentUserId());
        UserBean user = uib.getUserBean();
        uib.setUserBean(null);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(uib);
        Map map = om.readValue(json, Map.class);
        map.put("userRegTime", user.getCreateTime());
        map.put("userName", user.getNickname());
        map.put("userEmail", user.getUsername());
        map.remove("userBean");
        return map;
    }

    /**
     * 修改用户信息相关类
     *
     * @param data JSON字符串{type:'xxxxx',value:'xxxxx'},其中type表示字段名,value表示字段值
     * @return 表示修改结果的JSON(Map)对象,{status:'true/false'}
     * @throws ParseException 日期字符串解析失败
     */
    @PostMapping("/toChangePerInfo")
    @ResponseBody
    public Map changeUserInfo(@RequestBody Map data) throws ParseException {
        Map json = new HashMap();
        json.put("type", data.get("type"));
        json.put("value", data.get("value"));
        UserBean ub = userService.findUserById(GlobalVariableHolder.getCurrentUserId());
        switch (data.get("type").toString()) {
            case "userName":
                ub.setNickname(data.get("value").toString());
                userService.updateUser(ub);
                break;
            case "userSex":
                ub.getUserInfoBean().setUserSex(data.get("value").equals("1"));
                userInfoService.saveUserInfo(ub.getUserInfoBean());
                break;
            case "userBirth":
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
                Date date = simpleDateFormat.parse(data.get("value").toString());
                ub.getUserInfoBean().setUserBirth(date);
                userInfoService.saveUserInfo(ub.getUserInfoBean());
                break;
            case "userPhone":
                ub.getUserInfoBean().setUserPhone(data.get("value").toString());
                userInfoService.saveUserInfo(ub.getUserInfoBean());
                break;
            case "userHospital":
                ub.getUserInfoBean().setUserHosipital(data.get("value").toString());
                userInfoService.saveUserInfo(ub.getUserInfoBean());
                break;
        }
        json.put("status", true);
        return json;
    }

    /**
     * 校验密码是否正确
     *
     * @param data JSON(Map)对象,{oldPass:'密码'}
     * @return 表示结果的JSON(Map)对象,{status:'true/false',errorMsg:'错误信息'}
     */
    @PostMapping("/toVerifyPass")
    @ResponseBody
    public Map verifyPass(@RequestBody Map<String, String> data) {
        Map json = new HashMap();
        UserBean ub = userService.findUserById(GlobalVariableHolder.getCurrentUserId());
        String oldPss = passwordEncoder.encodePassword(data.get("oldPass"), null);
        if (!ub.getPassword().equals(oldPss)) {
            json.put("status", false);
            json.put("errorMsg", "旧密码不正确");
        } else {
            json.put("status", true);
        }
        return json;
    }

    /**
     * 校验旧密码,若校验成功则写入新密码
     *
     * @param data JSON(Map)对象,{oldPass:'旧密码',newPass:'新密码',repeatPass:'重复的新密码'}
     * @return 表示结果的JSON(Map)对象,{status:'true/false',errorMsg:'错误信息'}
     */
    @PostMapping("/toChangePass")
    @ResponseBody
    public Map changePass(@RequestBody Map<String, String> data) {
        Map json = new HashMap();
        if (StringUtils.isEmpty(data.get("oldPass"))) {
            json.put("status", false);
            json.put("errorMsg", "旧密码不能为空");
            return json;
        }
        if (StringUtils.isEmpty(data.get("newPass"))) {
            json.put("status", false);
            json.put("errorMsg", "新密码不能为空");
            return json;
        }
        if (StringUtils.isEmpty(data.get("repeatPass"))) {
            json.put("status", false);
            json.put("errorMsg", "请重复输入新密码");
            return json;
        }
        if (!data.get("newPass").equals(data.get("repeatPass"))) {
            json.put("status", false);
            json.put("errorMsg", "两次输入的密码不一致");
            return json;
        }

        UserBean ub = userService.findUserById(GlobalVariableHolder.getCurrentUserId());
        String oldPss = passwordEncoder.encodePassword(data.get("oldPass"), null);
        String newPass = passwordEncoder.encodePassword(data.get("newPass"), null);

        if (!ub.getPassword().equals(oldPss)) {
            json.put("status", false);
            json.put("errorMsg", "旧密码不正确");
            return json;
        }
        ub.setPassword(newPass);
        userService.updateUser(ub);
        json.put("status", true);
        return json;
    }

    /**
     *  获取当前用户的登录记录
     * @return JSON(Map)对象,{loginRecord:[{}]}
     */
    @GetMapping("/toGetLoginRecord")
    @ResponseBody
    public Map getLoginRecord() {
        Map json = new HashMap();
        List<LoginLog> logs = null;
        logs = loginLogService.getLoginLogs(GlobalVariableHolder.getCurrentUserId());
        Iterator<LoginLog> it = logs.iterator();
        while (it.hasNext()) {
            it.next().setUserBean(null);
        }
        json.put("loginRecord", logs);
        return json;
    }


    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserService userService;

    @Autowired
    LoginLogService loginLogService;

    Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
}
