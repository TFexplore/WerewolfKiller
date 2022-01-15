package com.example.werewolfkiller.woveskill.data;

public class Player {
    private String name;//昵称
    private String avatar;//头像

    private int num;//位置编号

    private Role role;

    private Integer uid;



    public Player(String name, String avatar, int num){
        this.avatar=avatar;
        this.name=name;
        this.num=num;
        this.role=new Role(" "," ",1);
    }

    public Player() {

    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
