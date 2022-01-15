package com.example.werewolfkiller.woveskill.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Record {
    HashMap<Integer, Player> users;//记录每局游戏的身份情况，key为位置编号，默认12个位置；

    List<List<Event>> nightEvents;//一局游戏的记录，元素为每一天的记录，分为白天与黑夜，index为天数

    List<List<Event>> dayEvents;

    int day;//天数

    public Record(){
        this.dayEvents=new ArrayList<>();
        this.nightEvents=new ArrayList<>();
    }

    public void add_nightEvent(int day,Event event){
        if (this.day<day){
            this.day++;
            List<Event> events=new ArrayList<>();
            nightEvents.add(day,events);
        }
        nightEvents.get(day).add(event);
    }
    public void add_dayEvent(int day,Event event){
        if (this.day<day){
            this.day++;
            List<Event> events=new ArrayList<>();
            dayEvents.add(day,events);
        }
        dayEvents.get(day).add(event);
    }

    public List<Event> get_dayEvents(int day){

        return this.dayEvents.get(day);
    }
    public List<Event> get_nightEvents(int day){

        return this.nightEvents.get(day);
    }




}
