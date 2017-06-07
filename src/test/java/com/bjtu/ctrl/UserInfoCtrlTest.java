package com.bjtu.ctrl;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.config.SpringBoot;
import com.bjtu.service.UserInfoService;
import com.bjtu.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by gimling on 17-6-5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBoot.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserInfoCtrlTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserInfoService userInfoService;

    @MockBean
    UserService userService;

    @Before
    public void setUp(){
        UserInfoBean uib = new UserInfoBean();
        UserBean ub = new UserBean();
        ub.setCreateTime(new Date(1496680335404L));
        ub.setNickname("黄XX");
        ub.setUsername("gimling");
        uib.setId(1L);
        uib.setUserSex(false);
        uib.setUserBirth(new Date(1496680335404L));
        uib.setUserHosipital("北京交通大学校医院");
        uib.setUserPhone("17801020521");
        uib.setUserBean(ub);
        ub.setUserInfoBean(uib);
        given(this.userService.getCurrentUserId()).willReturn(1L);
        given(this.userInfoService.getUserInfoByUID(1L)).willReturn(uib);
    }



    @Test
    public void getPerInfo() throws Exception {
        String expect = "{\"id\":1,\"userSex\":false,\"userPhone\":\"17801020521\",\"userBirth\":1496680335404,\"userHospital\":\"北京交通大学校医院\",\"userRegTime\":1496680335404,\"userName\":\"黄XX\",\"userEmail\":\"gimling\"}";
        this.mvc.perform(get("/toGetPerInfo").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(content().string(expect));
    }

    @Test
    public void changeUserInfo() throws Exception {

    }

    @Test
    public void showData() throws Exception {
    }

    @Test
    public void listData() throws Exception {
    }

    @Test
    public void verifyPass() throws Exception {
    }

    @Test
    public void changePass() throws Exception {
    }

    @Test
    public void getLoginRecord() throws Exception {
    }

}