package com.example.werewolfkiller.activity;

import com.baidu.mapapi.model.LatLng;

public class Activity {

    String userName;
    String avId;

    Integer cnum;//响应人数
    String distence;
    Integer comid;
    Integer userid;
    String ctime;
    String title;
    Integer pnum;//总人数
    Integer themeid;
    String ptime;
    Double latitude;
    Double longitude;
    String plocate;
    String content;

    public Activity() {
    }

    public Activity(String title, String local, String ptime, String distence, Integer num_all, Integer num) {
        this.title = title;
        this.plocate = local;
        this.ptime = ptime;
        this.distence = distence;
        this.pnum = num_all;
        this.cnum = num;
    }
    public LatLng getLatlng(){
        if (latitude==null||longitude==null){
            return new LatLng(0,0);
        }
        return new LatLng(latitude,longitude);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvId() {
        return avId;
    }

    public void setAvId(String avId) {
        this.avId = avId;
    }

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    public String getDistence() {
        return distence;
    }

    public void setDistence(String distence) {
        this.distence = distence;
    }

    public Integer getComid() {
        return comid;
    }

    public void setComid(Integer comid) {
        this.comid = comid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public Integer getThemeid() {
        return themeid;
    }

    public void setThemeid(Integer themeid) {
        this.themeid = themeid;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocal() {
        return plocate;
    }

    public void setLocal(String local) {
        this.plocate = local;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
