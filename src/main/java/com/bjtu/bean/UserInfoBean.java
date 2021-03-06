package com.bjtu.bean;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gimling on 2017/4/9.
 */
@Entity
@Table(name = "medical_userinfo")
@DynamicUpdate
public class UserInfoBean implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *性别
     *false---男,true---女
     */
    @Column
    private Boolean userSex;

    //电话
    @Column(length = 20)
    private String userPhone;

    //生日
    @Column
    @Temporal(TemporalType.DATE)
    private Date userBirth;

    //所属医院
    @Column(length = 20)
    private String userHospital;


    @OneToOne(targetEntity = UserBean.class)
    @JoinColumn(name = "uid",referencedColumnName = "id",unique = true)
    UserBean userBean;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getUserSex() {
        return userSex;
    }

    public void setUserSex(Boolean userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Date getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(Date userBirth) {
        this.userBirth = userBirth;
    }


    public String getUserHospital() {
        return userHospital;
    }

    public void setUserHosipital(String userHospital) {
        this.userHospital = userHospital;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
