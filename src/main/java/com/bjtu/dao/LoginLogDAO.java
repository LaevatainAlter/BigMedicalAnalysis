package com.bjtu.dao;

import com.bjtu.bean.LoginLogBean;

import java.util.List;

/**
 * Created by gimling on 17-4-21.
 */
public interface LoginLogDAO {

    boolean saveLoginLog(LoginLogBean ll);

    List<LoginLogBean> getLoginlogs(Long uid);
}
