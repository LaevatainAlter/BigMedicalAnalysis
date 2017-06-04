package com.bjtu.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by gimling on 17-6-4.
 */
@Entity
@Table(name = "medical_upload_record")
public class UploadRecordBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*上传时的文件名*/
    @Column(length = 50)
    private String originName;

    /*服务器分配的UUID*/
    @Column(length = 50)
    private String uuid;

    /*别称*/
    @Column(length = 50)
    private String name;

    /*上传者唯一标识符*/
    @ManyToOne(targetEntity = UserBean.class)
    @JoinColumn(name = "uid",referencedColumnName = "id")
    private UserBean userBean;

    /*上传时间*/
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    /*解析是否完成*/
    @Column
    private Boolean isAnalysis = false;

    /*是否可用*/
    @Column
    private Boolean enabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getAnalysis() {
        return isAnalysis;
    }

    public void setAnalysis(Boolean analysis) {
        isAnalysis = analysis;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
