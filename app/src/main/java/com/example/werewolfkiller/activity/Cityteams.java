package com.example.werewolfkiller.activity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Cityteams)实体类
 *
 * @author makejava
 * @since 2021-12-13 09:56:51
 */

public class Cityteams implements Serializable {

    //@ApiModelProperty("帖子ID")
    private Integer comid;
    //@ApiModelProperty("贴主ID")
    private Integer userid;
    //@ApiModelProperty("帖子标题")
    private String title;
   // @ApiModelProperty("发布时间，时间戳")
    private Date ctime;
    //@ApiModelProperty("主题分类ID，区分不同桌游")
    private Integer themeid;
   // @ApiModelProperty("0为组队，1为讨论，后续可以分得更清楚")
    private Integer ctype;
   // @ApiModelProperty("评论详细内容")
    private String content;
    //@ApiModelProperty("目标集结人数")
    private Integer pnum;
   // @ApiModelProperty("已经响应人数")
    private Integer cnum;
   // @ApiModelProperty("预定集结位置")
    private Double plocate;
   // @ApiModelProperty("发帖位置")
    private Double clocate;
    //@ApiModelProperty("预定集结时间")
    private Date ptime;



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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Integer getThemeid() {
        return themeid;
    }

    public void setThemeid(Integer themeid) {
        this.themeid = themeid;
    }

    public Integer getCtype() {
        return ctype;
    }

    public void setCtype(Integer ctype) {
        this.ctype = ctype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    public Double getPlocate() {
        return plocate;
    }

    public void setPlocate(Double plocate) {
        this.plocate = plocate;
    }

    public Double getClocate() {
        return clocate;
    }

    public void setClocate(Double clocate) {
        this.clocate = clocate;
    }

    public Date getPtime() {
        return ptime;
    }

    public void setPtime(Date ptime) {
        this.ptime = ptime;
    }
}