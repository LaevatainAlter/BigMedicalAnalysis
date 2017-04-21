package com.bjtu.ctrl;

import com.bjtu.bean.UserBean;
import com.bjtu.service.UserService;
import com.bjtu.util.IPHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gimling on 2017/4/14.
 */
//登录.注册的控制器
@Controller
public class EntranceCtrl {

    private static Logger logger = LoggerFactory.getLogger(EntranceCtrl.class);

    @GetMapping("/login")
    public String showloginPage(HttpServletRequest request) {

        String ip = IPHelper.getRealIp(request);

        return "index";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    //处理注册
    @PostMapping("/register")
    @ResponseBody
    @Transactional
    public Map processRegister(@RequestBody Map<String, String> map,
                               HttpSession session
    ) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Map json = userService.registerValidate(map);
        if(json==null){
            json = userService.processRegister(map);
        }
        return json;
    }




    @Autowired
    UserService userService;
}
