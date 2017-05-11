package com.bjtu.dao;

import com.bjtu.bean.LoginLog;

import java.util.List;

/**
 * Created by gimling on 17-4-21.
 */
public interface LoginLogDAO {

    public void saveLoginLog(LoginLog ll);

    public List<LoginLog> getLoginlogs(Long uid);
}
