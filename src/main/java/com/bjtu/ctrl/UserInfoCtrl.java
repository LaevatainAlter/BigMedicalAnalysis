package com.bjtu.ctrl;

import com.bjtu.bean.UserBean;
import com.bjtu.bean.UserInfoBean;
import com.bjtu.service.UserInfoService;
import com.bjtu.service.UserService;
import com.bjtu.util.GlobalVariableHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by gimling on 17-4-21.
 */
//处理用户信息
@Controller
public class UserInfoCtrl {

    //@PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/toGetPerInfo")
    @ResponseBody
    public UserInfoBean getPerInfo(){
        UserInfoBean uib = userInfoService.getUserInfoByUID(GlobalVariableHolder.getCurrentUserId());
        uib.setUserBean(null);
        return uib;
    }

    @PostMapping("/toChangePerInfo")
    @ResponseBody
    public Map changeUserInfo(@RequestBody UserInfoBean uib){
        UserBean ub = userService.findUserById(1L);
        ub.getUserInfoBean().setUserHosipital(uib.getUserHosipital());
        userInfoService.saveUserInfo(ub.getUserInfoBean());
        return null;
    }

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserService userService;
}
