package com.example.werewolfkiller.woveskill.data;

import java.util.HashMap;
import java.util.List;
/*
1.创建房间：Player master，token，configure，list site
2，加入房间：token，key_room，Player
3.重新发牌：
 */

public class Room {

    Integer key_Room;//房间密码
    Integer[] configure;//房间配置数组

    Player master;//房主

    HashMap<Integer, Player> users;//key为位置编号，默认12个位置；

    List<Integer> locate_role;

    Integer data;//天数
    Integer stage;//当天的阶段
    Integer times;//游戏局数，天数大于2天为一次有效局

    List<Record> records;//房间每一局的的记录

    public Room(Integer key_Room, Integer[] configure, Player master) {
        this.key_Room = key_Room;
        this.configure = configure;
        this.master = master;
    }

    public Integer getKey_Room() {
        return key_Room;
    }

    public void setKey_Room(Integer key_Room) {
        this.key_Room = key_Room;
    }

    public Integer[] getConfigure() {
        return configure;
    }

    public void setConfigure(Integer[] configure) {
        this.configure = configure;
    }

    public Player getMaster() {
        return master;
    }

    public void setMaster(Player master) {
        this.master = master;
    }

    public HashMap<Integer, Player> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, Player> users) {
        this.users = users;
    }

    public List<Integer> getLocate_role() {
        return locate_role;
    }

    public void setLocate_role(List<Integer> locate_role) {
        this.locate_role = locate_role;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
