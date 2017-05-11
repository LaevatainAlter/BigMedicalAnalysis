package com.bjtu.servlet;

import com.google.code.kaptcha.servlet.KaptchaServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by Gimling on 2017/4/7.
 */
@WebServlet(urlPatterns = "/kaptcha",
        initParams = {
                @WebInitParam(name = "kaptcha.textproducer.char.length", value = "4"),
                @WebInitParam(name = "kaptcha.textproducer.font.names", value = "彩云,宋体,楷体,微软雅黑"),
                @WebInitParam(name = "kaptcha.obscurificator.impl", value = "com.google.code.kaptcha.impl.ShadowGimpy")
        })
public class KaptchaConfig extends KaptchaServlet {


}
