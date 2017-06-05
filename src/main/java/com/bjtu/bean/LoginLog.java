package com.bjtu.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by gimling on 17-4-20.
 */
@Entity
@Table(name = "medical_login_log")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = UserBean.class)
    @JoinColumn(name = "uid",referencedColumnName = "id")
    private UserBean userBean;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordTime = new Date();

    @Column(length = 20)
    private String recordIP;

    @Column(length = 50)
    private String recordPosition;

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

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordIP() {
        return recordIP;
    }

    public void setRecordIP(String recordIP) {
        this.recordIP = recordIP;
    }

    public String getRecordPosition() {
        return recordPosition;
    }

    public void setRecordPosition(String recordPosition) {
        this.recordPosition = recordPosition;
    }
}
