package com.example.werewolfkiller.woveskill.data;

import com.example.werewolfkiller.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Mould {

    public HashMap<Integer, Role> roles;
    public List<com.example.werewolfkiller.modle.Role> roleList;
    public List<Player> players;
    public static Mould instanse;

    public static synchronized Mould getMould(){
    if (instanse==null){
        instanse=new Mould();
    }
        return instanse;
    }
    private Mould(){

    this.roles=new HashMap<>();
    roleList=new ArrayList<>();
    players =new ArrayList<>();

    rolesInit();
    roleListInit();
    userInit();
        //System.out.println(roles.get(1).getName());
    }
    private void  rolesInit(){
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

        Role villager=new Role("村民","可以在白天发言讨论，参与投票",1);
        villager.setAbility(0);//无技能;
        roles.put(villager.getNum(),villager);

        Role seer=new Role("预言家","每晚可查看一名玩家是好人还是狼",2);
        seer.setAbility(6);
        seer.setSkills(6);
        roles.put(seer.getNum(),seer);

        Role witch=new Role("女巫","拥有一瓶解药和一瓶毒药",3);
        witch.setAbility(5);//1解药，2毒药，3两瓶药都在
        witch.setSkills(3);
        roles.put(witch.getNum(),witch);

        Role hunter=new Role("猎人","死后可以开枪带走一名玩家",4);
        hunter.setAbility(1);
        hunter.setSkills(4);
        roles.put(hunter.getNum(),hunter);

        Role rider=new Role("骑士","发言阶段可以选择一名玩家冲锋，若其为狼人则该玩家死亡，否则自身死亡",5);
        rider.setAbility(1);
        rider.setSkills(5);
        roles.put(rider.getNum(),rider);

        Role guard=new Role("守卫","每晚可选择一名玩家不被狼人杀害，不能连续两天守护同一玩家",6);
        guard.setAbility(4);
        guard.setSkills(7);
        roles.put(guard.getNum(),guard);

        Role werewolf=new Role("狼人","每晚杀死一名玩家",7);
        werewolf.setAbility(7);
        werewolf.setSkills(11);
        roles.put(werewolf.getNum(),werewolf);

        Role whiteWolfKing=new Role("白狼王","自爆后可以直接杀死一名玩家",8);
        whiteWolfKing.setAbility(7);
        whiteWolfKing.setSkills(12);
        roles.put(whiteWolfKing.getNum(),whiteWolfKing);

        Role wolfKing=new Role("狼王","死后可以枪杀一名玩家（被女巫毒死除外）",9);
        wolfKing.setAbility(7);
        werewolf.setSkills(13);
        roles.put(wolfKing.getNum(),wolfKing);

        Role Lmr=new Role("狼美人","死后可以枪杀一名玩家（被女巫毒死除外）",10);
        Lmr.setAbility(7);
        Lmr.setSkills(14);
        roles.put(Lmr.getNum(),Lmr);

    }
   private void roleListInit(){
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_cunming, "描述：无特殊技能，主要靠推理分析找出狼人", "村民", "#b2ebf2"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_lieren, "描述：当猎人死亡时可开枪击杀一名玩家，但当死于女巫的毒药或者白狼王自爆带走时，不能开枪", "猎人", "#80deea"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_nvwu, "描述：女巫有一瓶解药和一瓶毒药。同一晚不能使用两瓶药且可对自己用药", "女巫", "#4dd0e1"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_yuyanjia, "描述：每晚可以查验一名玩家身份，知道他是狼人还是好人", "预言家", "#26c6d2"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_qishi, "骑士可以在白天竞选结束后，放逐投票之前，随时翻牌决斗场上除自己以外的任意一位玩家", "骑士", "#29d6f6"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_shouwei,"每晚可选择一名玩家不被狼人杀害，不能连续两天守护同一玩家","守卫","#09bedf"));//R.mipmap.icon_shouwei,"每晚可选择一名玩家不被狼人杀害，不能连续两天守护同一玩家","#09bedf"
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.lang, "描述：每天夜里可以杀死一个人。", "普通狼", "#81d4fa"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_lang2, "能力：除狼人能力外，被公投出局时可选择发动技能带走一人", "狼枪", "#b3e5fc"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_lang3, "描述：属于狼人阵营，白狼王可以在白天自爆的时候，选择带走一名玩家，非自爆出局不得发动技能", "白狼王", "#d0d9ff"));
       roleList.add(new com.example.werewolfkiller.modle.Role(R.mipmap.icon_lang6, "描述”狼美人在夜里可以魅惑一人，天亮后，如果狼美人被放逐出局或者被猎人射杀，被魅惑的玩家跟随狼美人一起出局", "狼美人", "#afbfff"));
    }
    private void userInit(){
        String[] strings={
               "务实的小熊猫",
               "光亮的灰狼",
               "留胡子的心锁",
               "忧心的柠檬",
               "善良的超短裙",
               "坚定的小天鹅",
               "寂寞的含羞草",
               "高大的音响",
               "欢呼的大山",
               "谦让的仙人掌",
               "威武的美女",
               "苗条的黄豆",
               "还单身的枕头",
               "矮小的未来",
               "年轻的蜡烛",
               "超级的西装",
               "虚幻的网络",
               "柔弱的往事",
               "背后的冬天",
               "甜美的短靴 ", };
        Random random=new Random();

        for (int i = 0; i <12; i++) {
            int n=random.nextInt(20);
            int a=random.nextInt(12)+1;
            players.add(i,new Player(strings[n],""+(i+1),-1));
        }

    }
    public Player getUser(int index){
        Player player = players.get(index);
        player.setNum(index+1);
        return player;
    }



}
