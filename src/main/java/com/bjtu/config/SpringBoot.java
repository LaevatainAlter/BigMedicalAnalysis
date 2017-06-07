package com.bjtu.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


/**
 * Created by Gimling on 2017/4/7.
 */
@SpringBootApplication
@ComponentScans({
        @ComponentScan("com.bjtu.ctrl"),
        @ComponentScan("com.bjtu.dao.impl"),
        @ComponentScan("com.bjtu.service"),
        @ComponentScan("com.bjtu.config")
})
@EntityScan(basePackages = {"com.bjtu.bean"})
@EnableCaching
public class SpringBoot extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBoot.class);
    }

}
