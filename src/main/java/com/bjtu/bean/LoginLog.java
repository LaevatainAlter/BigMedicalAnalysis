package com.bjtu.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by gimling on 17-4-20.
 */
@Entity
@Table(name = "LoginLog")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = UserBean.class)
    @JoinColumn(name = "uid",referencedColumnName = "id")
    private UserBean userBean;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date time = new Date();

    @Column(length = 20)
    private String ip;

    @Column(length = 50)
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
