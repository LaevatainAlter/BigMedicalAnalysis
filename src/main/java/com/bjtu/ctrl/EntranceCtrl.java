package com.bjtu.ctrl;

import com.bjtu.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Gimling on 2017/4/14.
 */
//登录.注册的控制器
@Controller
public class EntranceCtrl {

    private static Logger logger = LoggerFactory.getLogger(EntranceCtrl.class);

    /**
     * 显示登录界面
     * @return '/WEB-INF/pages/index.html'的视图
     */
    @GetMapping("/login")
    public String showloginPage() {
        return "index";
    }

    /**
     * 显示注册界面
     * @return '/WEB-INF/pages/register.html'的视图
     */
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    /**
     * 处理注册请求
     * @param map 前端传过来的JSON数据
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/register")
    @ResponseBody
    public Map processRegister(@RequestBody Map<String, String> map
    ) throws JsonProcessingException {
        /*JSON数据校验*/
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

    @Autowired
    ObjectMapper mapper;
}
