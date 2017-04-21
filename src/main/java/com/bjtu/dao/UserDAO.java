package com.bjtu.dao;

import com.bjtu.bean.UserBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Created by Gimling on 2017/4/8.
 */
public interface UserDAO{

    /**
     *
     * @param name 用户名，即邮箱或者昵称
     * @return
     */
    UserBean findUserByName(String name);

    /**
     *
     * @param name 邮箱
     * @return
     */
    UserBean findUserByUsername(String name);


    /**
     *
     * @param name 昵称
     * @return
     */
    UserBean findUserByNickname(String name);


    Boolean saveUserBean(UserBean ub);


    UserBean findUserById(Long id);

}