package com.bjtu.ctrl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Gimling on 2017/4/7.
 */
@Controller
public class IndexCtrl {



    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public String user(){
        return "home/register";
    }

    @PostMapping(value = "/post")
    @ResponseBody
    public String post(HttpServletRequest request){
        return "success";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/home")
    public String homePage(){
        return "project/home";
    }
}
