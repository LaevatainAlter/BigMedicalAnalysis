package com.bjtu.ctrl;

import com.bjtu.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Gimling on 2017/4/14.
 */
//登录.注册的控制器
@Controller
public class EntranceCtrl {

    private static Logger logger = LoggerFactory.getLogger(EntranceCtrl.class);

    @GetMapping("/login")
    @PreAuthorize("permitAll()")
    public String showloginPage(HttpServletRequest request) {
        request.getSession(true);
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


    @GetMapping("*")
    public String defaultPage(){
        return "redirect:/home";
    }




    @Autowired
    UserService userService;
}
