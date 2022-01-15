package com.example.werewolfkiller.woveskill.data;

public class Role {
    private String name;//角色名

    private String desc;//描述

    private int num;//id

    private int state=1;//状态：0死亡，1存活

    /*
       夜晚睁眼优先级：
       7：狼人
       6：女巫
       5：预言家
       4：守卫
     */
    private int ability;//能力：优先级和类别

    /*
       女巫  :1毒，2解药，3两瓶药都在
       猎人  ：4//白天，被女巫毒死时置为0；
       骑士  ：5//白天
       预言家：6
       守卫   ：7
       狼    ：11
       白狼王：12//白天使用
       狼王   ：13//白天
       狼美人  ：14//
     */
    private int skills;//特殊技能：白狼王，猎人等；

    private int dest;//目标：投票或技能目标


    //private int priority;

    public Role(String name, String desc, int num){
        this.name=name;
        this.desc=desc;
        this.num = num;
    }

    public Role() {
    }

    public int getSkills() {
        return skills;
    }

    public void setSkills(int skills) {
        this.skills = skills;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAbility() {
        return ability;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }

}
