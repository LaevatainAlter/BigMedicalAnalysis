package com.bjtu.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by gimling on 17-4-20.
 */
@Entity
@Table(name = "medical_login_log")
public class LoginLogBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = UserBean.class)
    @JoinColumn(name = "uid",referencedColumnName = "id",nullable = false)
    private UserBean userBean;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordTime = new Date();

    @Column(length = 20)
    private String recordIP;

    @Column(length = 50)
    private String recordPosition;

    public LoginLogBean() {
    }

    public LoginLogBean(String recordIP) {
        this.recordIP = recordIP;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginLogBean)) return false;

        LoginLogBean that = (LoginLogBean) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getUserBean() != null ? !getUserBean().equals(that.getUserBean()) : that.getUserBean() != null)
            return false;
        if (getRecordTime() != null ? !getRecordTime().equals(that.getRecordTime()) : that.getRecordTime() != null)
            return false;
        if (getRecordIP() != null ? !getRecordIP().equals(that.getRecordIP()) : that.getRecordIP() != null)
            return false;
        return getRecordPosition() != null ? getRecordPosition().equals(that.getRecordPosition()) : that.getRecordPosition() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUserBean() != null ? getUserBean().hashCode() : 0);
        result = 31 * result + (getRecordTime() != null ? getRecordTime().hashCode() : 0);
        result = 31 * result + (getRecordIP() != null ? getRecordIP().hashCode() : 0);
        result = 31 * result + (getRecordPosition() != null ? getRecordPosition().hashCode() : 0);
        return result;
    }
}
