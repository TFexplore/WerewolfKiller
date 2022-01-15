package com.example.werewolfkiller.woveskill;

import android.app.Activity;
import android.util.Log;
import com.example.werewolfkiller.woveskill.data.Mould;
import com.example.werewolfkiller.woveskill.data.Role;
import com.example.werewolfkiller.woveskill.operate.Operator;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import static com.tencent.open.log.SLog.TAG;

/*
初始化时，房主配置房间参数，并在创建成功后将参数上传服务器
参数包含：房间号，身份；其他用户输入房间号，请求服务器数据到本地，加入房间

 */

public class Game extends Thread {

    private int num=12;//总人数;
    private MediaUtils media;
    private Activity activity;
    private WoveskillViewModle woveskillViewModle;
    private List<Integer> configure;
    private int flag;
    Integer date;//天数
    Integer stage;//当天的阶段
    Integer times;//游戏局数，天数大于2天为一次有效局
    public Game(Activity activity, WoveskillViewModle woveskillViewModle, List<Integer> configure) {
        this.activity=activity;
        media=new MediaUtils(activity);
        this.woveskillViewModle=woveskillViewModle;
        this.configure=configure;
        num=configure.size();
        date=0;
        flag=-1;
    }
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < configure.size(); i++) {
            Log.d(TAG, "run: "+ Mould.getMould().roles.get(configure.get(i)).getName());
        }
        while (true){
           //开场语音
           flag=0;
           LockSupport.park();
           flag=1;
           media.dayclose();
            LockSupport.parkUntil(media.getDuration()+ System.currentTimeMillis());
            Role role;
           //角色语音
           for (int i = num-1; i >=0; i--) {
               role=Mould.getMould().roles.get(configure.get(i));
               while (i!=0&&role.getAbility()== Mould.getMould().roles.get(configure.get(i-1)).getAbility()){
                   i--;
               }

               if (role.getAbility()>=4){
                   Log.d(TAG, "run: for"+role.getAbility());
                   stage=role.getAbility();
                   media.open(role.getAbility());
                   LockSupport.parkUntil(media.getDuration()+ System.currentTimeMillis()+1*10000);
                   if (role.getAbility()==7){
                       //Operator.getINSTANS().eventPost(0,1005,0);
                       Operator.upkill();
                   }
                   media.close(role.getAbility());
                   LockSupport.parkUntil(media.getDuration()+ System.currentTimeMillis());
               }

           }
            Operator.getINSTANS().getResult();
           //结束语音
           if (date==0){
               media.dayopen1();
           }
           else media.dayopen2();
            Operator.getINSTANS().getRoom(1003);
            LockSupport.parkUntil(media.getDuration()+ System.currentTimeMillis());
           date++;
           stage=1;
           woveskillViewModle.data=date;
           Log.d(TAG, "run: ");
       }
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
