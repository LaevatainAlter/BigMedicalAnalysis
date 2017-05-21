package com.bjtu.service;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.config.SecurityConfig;
import com.bjtu.dao.UserDAO;
import com.bjtu.util.GlobalVariableHolder;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gimling on 17-4-21.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    UserDAO userDAO;

    private static String[][] errMsg = {
            {"username", "用户名不能为空"},
            {"password", "密码不能为空"},
            {"repeat-password", "请重复输入密码"},
            {"code", "验证码不能为空"}
    };

    public Map processRegister(Map<String, String> map) {
        UserBean ub = new UserBean();
        ub.setUsername(map.get("username"));
        ub.setPassword(SecurityConfig.getPasswordEncoder().encodePassword(map.get("password"), null));
        UserInfoBean uib = new UserInfoBean();
        ub.setUserInfoBean(uib);
        userDAO.saveUserBean(ub);
        Map json = new HashMap();
        json.put("status", true);
        return json;
    }

    public Map<String, String> registerValidate(Map<String, String> map) {

        Map json = null;
        for (int i = 0; i < errMsg.length; i++) {
            if ((json = checkNull(map.get(errMsg[i][0]), errMsg[i][0], errMsg[i][1])) != null) {
                return json;
            }
        }

        //验证码校验
        String kaptcha = map.get("code");
        String expect = (String) GlobalVariableHolder.getCurrentRequest().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!kaptcha.equalsIgnoreCase(expect)) {
            return generateErrorMap("code", "验证码错误");
        }

        //验证密码
        String password = map.get("password");
        String rePassword = map.get("repeat-password");
        if (!password.equals(rePassword)) {
            return generateErrorMap("repeat-password", "两次输入的密码不一致");
        }

        UserBean ub = userDAO.findUserByUsername(map.get("username"));
        if (ub != null) {
            return generateErrorMap("username", "邮箱已被使用");
        }
        return null;
    }

    public UserBean findUserById(Long uid) {
        return userDAO.findUserById(uid);
    }

    @Transactional
    public void updateUser(UserBean ub) {
        userDAO.update(ub);
        this.cleanUserBeanCache(ub.getId());
    }


    @CacheEvict(key = "#id")
    public void cleanUserBeanCache(Long id) { }

    private Map generateErrorMap(String field, String msg) {
        Map json = new HashMap<String, String>();
        json.put("status", false);
        json.put("errorMsg", msg);
        json.put("errorField", field);
        return json;
    }

    private Map checkNull(Object str, String field, String msg) {
        if (StringUtils.isEmpty(str)) {
            return generateErrorMap(field, msg);
        }
        return null;
    }


}
