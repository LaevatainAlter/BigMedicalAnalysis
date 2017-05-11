package com.bjtu.util;

import com.bjtu.bean.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Gimling on 2017/4/13.
 */
//获取一些全局变量
public  class GlobalVariableHolder {

    @Autowired
    private static ApplicationContext context;


    static public HttpServletRequest getCurrentRequest(){

        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static Long getCurrentUserId(){
        return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static Object getBean(String bean){
        return context.getBean(bean);
    }

    public static <T> T  getBean(Class<T> requiredType){
        return context.getBean(requiredType);
    }
}
