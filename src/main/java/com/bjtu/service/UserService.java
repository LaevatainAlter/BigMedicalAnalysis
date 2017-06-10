package com.bjtu.service;

import com.bjtu.bean.UserBean;
import com.bjtu.config.SecurityConfig;
import com.bjtu.dao.UserDAO;
import com.bjtu.util.GlobalVariableHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.bjtu.util.GlobalVariableHolder.getCurrentKaptcha;

/**
 * Created by gimling on 17-4-21.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    UserDAO userDAO;

    private static String[][] fieldInfo = {
            {"username", "用户名不能为空"},
            {"password", "密码不能为空"},
            {"repeat-password", "请确认您的密码"},
            {"code", "验证码不能为空"}
    };

    /**
     * 保存用户的注册信息
     * @param map
     * @return
     */
    @Transactional
    public Map processRegister(Map<String, String> map) {
        String username = map.get("username");
        /*MD5加密密码*/
        String password = SecurityConfig.getPasswordEncoder().encodePassword(map.get("password"), null);
        UserBean ub = new UserBean(username,password);
        userDAO.saveUserBean(ub);

        Map json = new LinkedHashMap();
        json.put("status", true);
        return json;
    }

    /**
     * 注册信息校验
     * @param map 用户的注册信息
     * @return
     */
    public Map<String, String> registerValidate(Map<String, String> map) {

        //校验Map(JSON)完整性
        for (int i = 0; i < fieldInfo.length; i++) {
            if(!map.containsKey(fieldInfo[i][0])&&map.get(fieldInfo[i][0])==null||StringUtils.isEmpty(map.get(fieldInfo[i][0]))){
                return generateErrorMap(fieldInfo[i][0], fieldInfo[i][1]);
            }
        }

        //验证码校验
        String kaptcha = map.get("code");
        String expect = getCurrentKaptcha();
        if (!kaptcha.equalsIgnoreCase(expect)) {
            return generateErrorMap("code", "验证码错误");
        }

        //验证密码
        String password = map.get("password");
        String rePassword = map.get("repeat-password");
        if (!password.equals(rePassword)) {
            return generateErrorMap("repeat-password", "两次输入的密码不一致");
        }

        /*检查邮箱是否占用*/
        UserBean ub = userDAO.findUserByUsername(map.get("username"));
        if (ub != null) {
            return generateErrorMap("username", "邮箱已被使用");
        }
        return null;
    }

    public UserBean findUserById(Long uid) {
        return userDAO.findUserById(uid);
    }

    public boolean updateUser(UserBean ub) {
        return userDAO.update(ub);
    }

    /**
     * 生成错误信息
     * @param field 发生错误的字段
     * @param msg 发生错误的信息
     * @return
     */
    private Map generateErrorMap(String field, String msg) {
        Map json = new LinkedHashMap();
        json.put("status", false);
        json.put("errorMsg", msg);
        json.put("errorField", field);
        return json;
    }

    public Long getCurrentUserId(){
        return GlobalVariableHolder.getCurrentUserId();
    }


}
