package com.example.werewolfkiller.woveskill;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.werewolfkiller.woveskill.data.Mould;
import com.example.werewolfkiller.woveskill.data.Player;
import com.example.werewolfkiller.woveskill.data.Record;
import com.example.werewolfkiller.woveskill.data.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WoveskillViewModle extends AndroidViewModel {

    Integer key_Room;//房间密码

    MutableLiveData<Integer> userNum;//总人数
    MutableLiveData<Integer> userIn;//玩家人数

    Player mMaster;//房主
    boolean master;//是否房主标识
    Integer num;
    Integer mNum;

    public HashMap<Integer, Player> users;//玩家map

    public List<Integer> configure;//配置
    List<Integer> locate_role;//角色map

    Integer data;//天数
    Integer stage;//当天的阶段
    Integer times;//游戏局数，天数大于2天为一次有效局

    List<Record> records;//房间每一局的的记录


    public WoveskillViewModle(@NonNull Application application) {
        super(application);
        userIn=new MutableLiveData<>();
        userNum=new MutableLiveData<>();
        master=false;
        users=new HashMap<>();
        locate_role=new ArrayList<>();
        userNum.setValue(0);
        userIn.setValue(0);
        num=0;
        mNum=0;

        for (int i = 0; i < 12; i++) {
            locate_role.add(i,0);
        }

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

    //master
    public Player getmMaster() {
        return users.get(num);
    }
    public void setMaster(boolean flag) {
        this.master = flag;
    }
    public boolean isMaster(){
        return master;
    }
    public Integer getNum(){
        return num;
    }
    public void setNum(Integer num){
        this.num=num;
    }
    public Integer getmNum() {
        return mNum;
    }//房主位置

    //key_room
    public Integer getKey_Room() {
        return key_Room;
    }
    public void setKey_Room(Integer key_Room) {
        this.key_Room = key_Room;
    }

    //configure
    public List<Integer> getConfigure() {
        return configure;
    }
    public void setConfigure(List<Integer> configure) {
        this.configure = configure;
    }

    //site_role
    public List<Integer> getLocate_role() {
        return locate_role;
    }
    public void setLocate_role(List<Integer> locate_role) {
        this.locate_role = locate_role;
    }
    public Role getRole(Integer index){
        return Mould.getMould().roles.get(locate_role.get(index-1));
    }
    public String getPerpson(Integer index){
        if(Mould.getMould().roles.get(locate_role.get(index-1)).getNum()>=7){
            return "好人";
        }
        else return "狼人";
    }
    public Role setRole(Integer index){
        return users.get(index).getRole();
    }

    //totalNum
    public void userAdd(){
        userNum.setValue(userNum.getValue()+1);
    }
    public void userSub(){
        userNum.setValue(userNum.getValue()-1);
    }
    public LiveData<Integer> getUserNum(){
        return this.userNum;
    }

    //userNum
    public void userInAdd(){
        userIn.setValue(userIn.getValue()+1);
    }
    public LiveData<Integer> getUserIn(){
        return this.userIn;
    }

    //site_users

    public void setUsers(HashMap<Integer, Player> users) {
        this.users = users;
    }

    public Player getPlayer(int index){
        return users.get(index);
    }
    public void  addUser(int index, Player player){
       this.users.put(index, player);
    }
    public boolean contained(Integer key){
        return users.containsKey(key);
    }

    public void MasterInit(Integer uid, Integer mUid){
        if (uid.equals(mUid)) this.master=true;
       users.forEach((key, player) -> {
           if (player.getUid().equals(uid)){
              num=key;
           }else if (player.getUid().equals(mUid)){
               mNum=key;
           }
        });
    }

}
