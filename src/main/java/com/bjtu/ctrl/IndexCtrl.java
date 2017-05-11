package com.bjtu.ctrl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Gimling on 2017/4/7.
 */
@Controller
@PreAuthorize("hasRole('ROLE_USER')")
public class IndexCtrl {



    @GetMapping("/user")
    public String user(){
        return "home/register";
    }


    @GetMapping("/home")
    public String homePage(){
        return "project/home";
    }

    @GetMapping("/person")
    public String personPage(){
        return "project/person";
    }
}
