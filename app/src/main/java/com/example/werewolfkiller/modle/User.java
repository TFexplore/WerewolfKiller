package com.example.werewolfkiller.modle;

public class User {
    /**
     * 用户id
     */
    private int uid;
    /**
     * 用户名
     */
    private String uname;
    /**
     * 年龄
     */
    private Integer uage;
    /**
     * 密码
     */
    private String upassword;
    /**
     * 登录名，手机号
     */
    private String utel;
    /**
     * 性别
     */
    private String usex;
    /**
     * 个性签名
     */
    private String upersonality;
    /**
     * 微信
     */
    private String uwechat;
    /**
     * QQ
     */
    private String uqq;
    /**
     * 等级
     */
    private Integer ulevel;

    private Integer uexp;

    private String uavatar;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getUage() {
        return uage;
    }

    public void setUage(Integer uage) {
        this.uage = uage;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel;
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public String getUpersonality() {
        return upersonality;
    }

    public void setUpersonality(String upersonality) {
        this.upersonality = upersonality;
    }

    public String getUwechat() {
        return uwechat;
    }

    public void setUwechat(String uwechat) {
        this.uwechat = uwechat;
    }

    public String getUqq() {
        return uqq;
    }

    public void setUqq(String uqq) {
        this.uqq = uqq;
    }

    public Integer getUlevel() {
        return ulevel;
    }

    public void setUlevel(Integer ulevel) {
        this.ulevel = ulevel;
    }

    public Integer getUexp() {
        return uexp;
    }

    public void setUexp(Integer uexp) {
        this.uexp = uexp;
    }

    public String getUavatar() {
        return uavatar;
    }

    public void setUavatar(String uavatar) {
        this.uavatar = uavatar;
    }
}
