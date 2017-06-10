package com.bjtu.dao;

import com.bjtu.bean.UserBean;
/**
 * Created by Gimling on 2017/4/8.
 */
public interface UserDAO{

    /**
     *通过用户名查找UserBean实体
     * @param name 用户名，即邮箱或者昵称
     * @return
     */
    UserBean findUserByName(String name);

    /**
     *通过邮箱查找UserBean实体
     * @param name 邮箱
     * @return
     */
    UserBean findUserByUsername(String name);

    /**
     * 通过昵称查找UserBean实体
     * @param name 昵称
     * @return
     */
    UserBean findUserByNickname(String name);


    Boolean saveUserBean(UserBean ub);


    UserBean findUserById(Long id);


    boolean update(UserBean ub);

}