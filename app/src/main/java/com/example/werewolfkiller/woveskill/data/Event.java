package com.example.werewolfkiller.woveskill.data;

import java.util.List;

public class Event {
    List<Integer> source;//发起人num位置

    Integer op;//操作：ability
    /*
    非事件：不加入事件记录
    天黑：1000//游戏天数+1，游戏阶段改变
    天亮：1001//游戏阶段改变，判断是否有人死亡，返回0或者死亡玩家的list<>,将死亡玩家的state改为0
    发起投票：1002：主要用于判断当前是否投票阶段，暂时不做处理
    结束投票：1004：统计投票情况并返回投票结果：将目标相同的1003事件合并后添加到event末尾；
    //根据事件类型对房间状态和事件做出处理
    事件：
    表决投票：1003
    使用技能：
       女巫  :1毒，2救，/3两瓶药都在
       猎人  ：4//白天，被女巫毒死时置为0；
       骑士  ：5//白天
       预言家：6
       守卫   ：7
       狼    ：11
       白狼王：12//白天使用
       狼王   ：13//白天
       狼美人  ：14//
     */

    List<Integer> dest;//目标

    public Event(List<Integer> source, Integer op, List<Integer> dest) {
        this.source = source;
        this.op = op;
        this.dest = dest;
    }

    public List<Integer> getSource() {
        return source;
    }

    public void setSource(List<Integer> source) {
        this.source = source;
    }

    public Integer getOp() {
        return op;
    }

    public void setOp(Integer op) {
        this.op = op;
    }

    public List<Integer> getDest() {
        return dest;
    }

    public void setDest(List<Integer> dest) {
        this.dest = dest;
    }
}
