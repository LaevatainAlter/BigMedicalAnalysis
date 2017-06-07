package com.bjtu.util;

import com.bjtu.bean.UserDetails;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
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

    /**
     * 获取当前处理的请求
     * @return
     */
    static public HttpServletRequest getCurrentRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取当前登录的用户的id
     * @return
     */
    public static Long getCurrentUserId(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext==null){
            return null;
        }
        Authentication authentication = securityContext.getAuthentication();
        if(authentication==null){
            return null;
        }
        Object principal = authentication.getPrincipal();
        Long id = ((UserDetails) principal).getId();
        return id;
    }

    /**
     * 获取当前session储存的验证码的值
     * @return
     */
    public static String getCurrentKaptcha(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes==null){
            return "";
        }
        return (String) GlobalVariableHolder.getCurrentRequest().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
    }


    public static Object getBean(String bean){
        return context.getBean(bean);
    }

    public static <T> T  getBean(Class<T> requiredType){
        return context.getBean(requiredType);
    }
}
