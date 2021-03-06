package com.bjtu.bean;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gimling on 2017/4/8.
 */
@Entity
@Table(name = "medical_user")
public class UserBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String nickname = "";

    @Column(length = 32)
    private String password;

    @Column
    private Boolean enabled = true;

    @Column
    @Temporal(TemporalType.DATE)
    private Date createTime = new Date();

    @Column
    private String role = "ROLE_USER";

    @OneToOne(targetEntity = UserInfoBean.class,mappedBy = "userBean")
    private UserInfoBean userInfoBean;


    @OneToMany(targetEntity = LoginLogBean.class,mappedBy = "userBean",fetch = FetchType.LAZY)
    @OrderBy("id desc ")
    private List<LoginLogBean> loginLog = new ArrayList<>();

    @OneToMany(targetEntity = UploadRecordBean.class,mappedBy = "userBean")
    @OrderBy("id desc ")
    @Size(max=10)
    private List<UploadRecordBean> UploadRecords = new ArrayList<>();

    public UserBean() {
    }

    public UserBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public List<LoginLogBean> getLoginLog() {
        return loginLog;
    }

    public void setLoginLog(List<LoginLogBean> loginLog) {
        this.loginLog = loginLog;
    }

    public List<UploadRecordBean> getUploadRecords() {
        return UploadRecords;
    }

    public void setUploadRecords(List<UploadRecordBean> uploadRecords) {
        UploadRecords = uploadRecords;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBean)) return false;

        UserBean userBean = (UserBean) o;

        if (!getId().equals(userBean.getId())) return false;
        if (!getUsername().equals(userBean.getUsername())) return false;
        if (!getNickname().equals(userBean.getNickname())) return false;
        if (!getPassword().equals(userBean.getPassword())) return false;
        if (!getEnabled().equals(userBean.getEnabled())) return false;
        if (!getCreateTime().equals(userBean.getCreateTime())) return false;
        if (!getRole().equals(userBean.getRole())) return false;
        if (!getUserInfoBean().equals(userBean.getUserInfoBean())) return false;
        if (!getLoginLog().equals(userBean.getLoginLog())) return false;
        return getUploadRecords().equals(userBean.getUploadRecords());
    }
}
