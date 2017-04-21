package com.bjtu.config;

import com.bjtu.ctrl.IndexCtrl;
import com.bjtu.ctrl.UserInfoCtrl;
import com.bjtu.dao.impl.UserDAOImpl;
import com.bjtu.service.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Created by Gimling on 2017/4/7.
 */
@SpringBootApplication
@ComponentScans({
        @ComponentScan(basePackageClasses = IndexCtrl.class),
        @ComponentScan(basePackageClasses = UserDAOImpl.class),
        @ComponentScan(basePackageClasses = UserService.class)
})
@EntityScan(basePackages = {"com.bjtu.bean"})
@EnableJpaRepositories(basePackages = {"com.bjtu.dao"})
public class SpringBoot extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBoot.class);
    }

}
