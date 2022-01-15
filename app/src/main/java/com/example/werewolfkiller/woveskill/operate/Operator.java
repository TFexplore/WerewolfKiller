package com.example.werewolfkiller.woveskill.operate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.werewolfkiller.MessageEvent;
import com.example.werewolfkiller.MyOkhttp;
import com.example.werewolfkiller.MyViewModle;
import com.example.werewolfkiller.modle.Message;
import com.example.werewolfkiller.woveskill.WoveskillViewModle;
import com.example.werewolfkiller.woveskill.data.Event;
import com.example.werewolfkiller.woveskill.data.Player;
import com.example.werewolfkiller.woveskill.data.Room;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Operator {

    public static Operator INSTANS = new Operator();
    private static String TAG = "-----------";
    FragmentActivity activity;
    static WoveskillViewModle woveskillViewModle;
    static MyViewModle myViewModle;
    static String token;
    static String id;
    static Integer slect;
    Room room;
    public Integer data;//天数
    public Integer stage;//当天的阶段
    public Integer times;//游戏局数，天数大于2天为一次有效局
    List<Event> dayEvents;
    List<Event> nightEvent;

    List<Message> messages;

    public Message deadMsg;
    public Message voteMsg;

    List<Integer> deadList;

    public Operator() {
        messages = new ArrayList<>();
        data = 0;
        stage = 0;
        times = 0;
    }

    public static Operator getINSTANS() {
        return INSTANS;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
        woveskillViewModle = new ViewModelProvider(activity).get(WoveskillViewModle.class);
        myViewModle = new ViewModelProvider(activity).get(MyViewModle.class);
        token = myViewModle.getToken();
        id = "" + woveskillViewModle.getKey_Room();
    }

    public static void setSlect(Integer slect) {
        Operator.slect = slect;
    }

    public static void putOut(String string) {
        Operator.getINSTANS().activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getINSTANS().activity.getApplicationContext(), string, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void updata() {//更新房间配置
        woveskillViewModle.setConfigure(Arrays.asList(room.getConfigure()));
        woveskillViewModle.setLocate_role(room.getLocate_role());
        woveskillViewModle.setUsers(room.getUsers());
        woveskillViewModle.setKey_Room(room.getKey_Room());
        woveskillViewModle.MasterInit(myViewModle.getUser().getUid(),room.getMaster().getUid());
    }

    public void getRoom(Integer type) {
        MyOkhttp.getINSTANCE().getRoom(id, token);
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请求失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();
                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    Map<Object, Object> data = (Map<Object, Object>) map.get("data");
                    room = gson.fromJson(gson.toJson(data.get("room")), Room.class);
                    getINSTANS().updata();
                    putOut("success");
                    switch (type) {
                        case 1001:
                            Vote();
                            break;
                        case 1002:
                            getINSTANS().activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   checkRole();
                                }
                            });
                            break;
                        case 1003:
                            daybegin();
                            break;
                        case 1004:
                            offVote();
                            break;
                        case 1005:
                            getINSTANS().activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Operator.Skills();
                                }
                            });
                            break;
                        case 1010:
                            EventBus.getDefault().post(new MessageEvent(1010,""));
                            break;

                        default:
                            putOut("getRoom:bad type");
                            break;
                    }
                } else {
                    putOut("getRoom:" + map.get("message"));
                }
            }
        });
    }//获取房间信息，更新房间信息，type为后续操作类型

    public void getRoom(String key) {
        MyOkhttp.getINSTANCE().getRoom(key, token);
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请求失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();
                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    Map<Object, Object> data = (Map<Object, Object>) map.get("data");
                    room = gson.fromJson(gson.toJson(data.get("room")), Room.class);
                    getINSTANS().updata();
                    putOut("success");
                    EventBus.getDefault().post(new MessageEvent(0, ""));
                } else {
                    putOut("" + map.get("message"));
                }
            }
        });
    }//获取房间信息，更新房间信息

    public void createRoom(String body, String token) {
        MyOkhttp.getINSTANCE().creatRoom(body, token);
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请求失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();
                Map rmap = gson.fromJson(res, Map.class);
                if ((Double) rmap.get("code") == 200) {

                    Map<Object, Object> data = (Map<Object, Object>) rmap.get("data");
                    Room room = gson.fromJson(gson.toJson(data.get("room")), Room.class);
                    woveskillViewModle.setKey_Room(room.getKey_Room());
                    putOut("success");
                    EventBus.getDefault().post(new MessageEvent(1, ""));
                } else if ((Double) rmap.get("code") == 500) {
                    putOut("faied");
                }
            }
        });

    }//创建房间

    public static void Redeal() {//重新发牌
        if (woveskillViewModle.getConfigure() == null) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < woveskillViewModle.configure.size(); i++) {
                list.add(i, 0);
            }
            woveskillViewModle.setLocate_role(list);
        }//初始化

        int num = woveskillViewModle.getConfigure().size();

        for (int i = 0; i < woveskillViewModle.getConfigure().size(); i++) {
            woveskillViewModle.getLocate_role().set(i, 0);
        }//置零

        Random r = new Random();
        int index;
        for (int i : woveskillViewModle.getConfigure()) {
            while (true) {
                index = r.nextInt(num);
                if (woveskillViewModle.getLocate_role().get(index) == 0) {
                    woveskillViewModle.getLocate_role().set(index, i);
                    break;
                }
            }
        }//随机位置

        MyOkhttp.getINSTANCE()
                .reStart(new Gson().toJson(woveskillViewModle.getLocate_role())
                        , "" + woveskillViewModle.getKey_Room()
                        , token);
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请求失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("tag", "onResponse: " + res);
                Gson gson = new Gson();
                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    putOut("success:" + map.get("message"));
                } else if ((Double) map.get("code") == 500) {
                    putOut("faied: " + map.get("message"));
                }
            }
        });//返回处理
    }//重新发牌

    public void slectSite(Integer num) {
        woveskillViewModle.setNum(num);
        Player player = new Player();
        player.setNum(num);
        player.setRole(woveskillViewModle.getRole(num));
        player.setUid(myViewModle.getUser().getUid());
        player.setName(myViewModle.getUser().getUname());
        player.setAvatar(myViewModle.getUser().getUavatar());
        MyOkhttp.getINSTANCE().addPlayer(new Gson().toJson(player), "" + woveskillViewModle.getKey_Room(), myViewModle.getToken());
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请求失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();

                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    Map<Object, Object> data = (Map<Object, Object>) map.get("data");
                    room = gson.fromJson(gson.toJson(data.get("room")), Room.class);
                    updata();
                    putOut("success");
                    EventBus.getDefault().post(new MessageEvent(1, ""));
                } else if ((Double) map.get("code") == 500) {
                    putOut("faied" + map.get("message"));
                }
            }
        });
    }//入座

    public void upSite(Integer num) {
        MyOkhttp.getINSTANCE().addPlayer(new Gson().toJson(woveskillViewModle.getPlayer(num)), "" + woveskillViewModle.getKey_Room(), myViewModle.getToken());
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请求失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();

                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    Map<Object, Object> data = (Map<Object, Object>) map.get("data");
                    room = gson.fromJson(gson.toJson(data.get("room")), Room.class);
                    updata();
                    putOut("操作成功");
                    EventBus.getDefault().post(new MessageEvent(1, ""));
                } else if ((Double) map.get("code") == 500) {
                    putOut("faied" + map.get("message"));
                }
            }
        });
    }//更新座位

    public void addTest(Integer num) {
        woveskillViewModle.getPlayer(num).setRole(woveskillViewModle.getRole(num));
        MyOkhttp.getINSTANCE().addPlayer(new Gson().toJson(woveskillViewModle.getPlayer(num)), "" + woveskillViewModle.getKey_Room(), myViewModle.getToken());
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请求失败，请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();

                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    Map<Object, Object> data = (Map<Object, Object>) map.get("data");
                    room = gson.fromJson(gson.toJson(data.get("room")), Room.class);
                    updata();
                    putOut("测试数据加入成功");
                    EventBus.getDefault().post(new MessageEvent(1, ""));
                } else if ((Double) map.get("code") == 500) {
                    putOut("faied" + map.get("message"));
                }
            }
        });
    }//入座

    public void checkRole() {
        if (woveskillViewModle.getLocate_role() != null && woveskillViewModle.getNum() != 0) {
            Dialog dialog = new AlertDialog.Builder(getINSTANS().activity).setTitle("查看身份")
                    .setMessage("你的身份是：" + woveskillViewModle.getRole(woveskillViewModle.getNum()).getName())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            slectSite(woveskillViewModle.getNum());
                            dialog.cancel();
                        }
                    })
                    .show();
        } else {
            putOut("请先选择位置");
        }
    }//查看身份1002

    public void dayNight() {
        //MyOkhttp.getINSTANCE().dayNight(""+woveskillViewModle.getKey_Room(),token);
       /* MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();

                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    putOut("success");
                } else if ((Double) map.get("code") == 500) {
                    putOut("faied"+map.get("message"));
                }
            }
        });
        */
        data++;
        stage = 0;
        for (int i = 1; i < woveskillViewModle.configure.size(); i++) {
            woveskillViewModle.setRole(i).setDest(0);
            upSite(i);
        }
    }//天黑

    public void daybegin() {
        // MyOkhttp.getINSTANCE().daybegin(""+woveskillViewModle.getKey_Room(),token);
        /*MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();

                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                        putOut("success");
                } else if ((Double) map.get("code") == 500) {
                    putOut("faied"+map.get("message"));
                }
            }
        });
         */
        stage = 1;

        if (deadList.size()<1){
            deadMsg=new Message("系统通知","","平安夜");
        }
        else {
            String dead = new Gson().toJson(deadList);
            deadMsg=new Message("系统通知", "", "昨夜" + dead + "号玩家死亡");
        }
    }//天亮//1003

    public void onVote() {
        woveskillViewModle.setRole(woveskillViewModle.getNum()).setState(1001);
        upSite(woveskillViewModle.getNum());
       /* MyOkhttp.getINSTANCE().onVote(""+woveskillViewModle.getKey_Room(),token);
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: "+response.body().string());
            }
        });

        */
    }//开启投票

    public void offVote() {
        woveskillViewModle.setRole(woveskillViewModle.getNum()).setState(1);
        upSite(woveskillViewModle.getNum());
        HashMap<Integer, List<Integer>> vote = new HashMap<>();
        for (int i = 1; i < woveskillViewModle.configure.size(); i++) {
            int dest = woveskillViewModle.setRole(i).getDest();
            if (dest != 0) {
                if (vote.containsKey(dest)) {
                    vote.get(dest).add(i);
                } else {
                    vote.put(dest, new ArrayList<>());
                    vote.get(dest).add(i);
                }
            }
        }
        Gson gson = new Gson();
        String msg = gson.toJson(vote);
        voteMsg = new Message("投票结果", "0000", msg);
        EventBus.getDefault().post(new MessageEvent(1003, ""));
        for (int i = 1; i < woveskillViewModle.configure.size(); i++) {
            woveskillViewModle.setRole(i).setDest(0);
            upSite(i);
        }
        return;
       /* MyOkhttp.getINSTANCE().offVote(""+woveskillViewModle.getKey_Room(),token);
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: "+response.body().string());
            }
        });

        */
    }//结束投票1004

    public void Vote(){
        if (room.getMaster().getRole().getState()!=1001){
            putOut("尚未开启投票");

        }else {
            woveskillViewModle.setRole(woveskillViewModle.getNum()).setDest(slect);
            upSite(woveskillViewModle.getNum());
            putOut("投票成功: "+slect);
        }
    }//投票1001
    public void More(){
        if (woveskillViewModle.setRole(woveskillViewModle.getNum()).getState()<0){
            EventBus.getDefault().post(new MessageEvent(1006,""));
        }
    }//死亡玩家查看其他玩家身份1006
    public void eventPost(Integer s, Integer op, Integer d) {
        String body;
        Map<Object, Object> map = new HashMap<>();
        map.put("dest", Collections.singletonList(s));
        map.put("op", op);
        map.put("source", Collections.singletonList(d));
        body = new Gson().toJson(map);
        Log.d(TAG, "eventPost: " + body);
        MyOkhttp.getINSTANCE().event(body, "" + woveskillViewModle.getKey_Room(), token);
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();

                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if ((Double) map.get("code") == 200) {
                    putOut("success");
                } else if ((Double) map.get("code") == 201) {

                } else if ((Double) map.get("code") == 500) {
                    putOut("faied" + map.get("message"));
                }
            }
        });
    }//事件提交

    public static void Skills() {//获取mMaster信息使用技能
        Log.d(TAG, "Skills: " + woveskillViewModle.getmMaster().getRole().getNum());
        switch (woveskillViewModle.getmMaster().getRole().getNum()) {
            case 2:
                skillYyj();
                break;
            case 3:
                skillNv();
                break;
            case 4:
                skillLr();
                break;
            case 5:
                skillQs();
                break;
            case 6:
                skillSw();
                break;
            case 7:
                skillWoves();
                break;
            case 8:
                skillWoves();
                //skillWhiteWoves();
                break;
            case 9:
                skillWoves();
                //skillWovesKing();
                break;
            case 10:
                skillWoves();
                break;
            default:
                break;
        }

    }

    static void skillNv() {//女巫，遍历玩家的状态查找狼刀；设置dest，更改dest状态：1被毒杀，2被救，11被刀
        if (woveskillViewModle.getmMaster().getRole().getSkills() > 1) {//解药提示窗
            Integer finalDest = getkill();
            Dialog dialog = new AlertDialog.Builder(getINSTANS().activity).setTitle("使用技能")
                    .setMessage("今晚：" + finalDest + " 号玩家被杀，是否使用解药")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (finalDest!=0){
                                //getINSTANS().eventPost(woveskillViewModle.getNum(),2, finalDest);
                                woveskillViewModle.getPlayer(woveskillViewModle.getNum()).getRole().setDest(slect);//修改自身dest
                                getINSTANS().upSite(woveskillViewModle.getNum());
                            }
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (slect==0){
                                Dialog dialog1 = new AlertDialog.Builder(getINSTANS().activity).setTitle("使用技能")
                                        .setMessage("选择目标使用毒药")
                                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                            else if (woveskillViewModle.getmMaster().getRole().getSkills() == 3 || woveskillViewModle.getmMaster().getRole().getSkills() == 1) {
                                Dialog dialog1 = new AlertDialog.Builder(getINSTANS().activity).setTitle("使用技能")
                                        .setMessage("目标：" + slect + " 号玩家,是否使用毒药")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                woveskillViewModle.getPlayer(woveskillViewModle.getNum()).getRole().setDest(slect);//修改自身dest
                                                getINSTANS().upSite(woveskillViewModle.getNum());

                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        }
                    })
                    .show();
        }else if (woveskillViewModle.getmMaster().getRole().getSkills() == 1||slect!=0) {//毒药提示窗
            Dialog dialog = new AlertDialog.Builder(getINSTANS().activity).setTitle("使用技能")
                    .setMessage("目标：" + slect + " 号玩家,是否使用毒药")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //getINSTANS().eventPost(woveskillViewModle.getNum(),1,slect);
                            woveskillViewModle.getmMaster().getRole().setSkills(0);
                            woveskillViewModle.getPlayer(woveskillViewModle.getNum()).getRole().setDest(slect);//修改自身dest
                            getINSTANS().upSite(woveskillViewModle.getNum());

                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }else putOut("请先选择目标");
    }

    static void skillYyj() {//预言家操作只修改自己的目标
        if (slect != 0) {
            woveskillViewModle.getPlayer(woveskillViewModle.getNum()).getRole().setDest(slect);
            getINSTANS().upSite(woveskillViewModle.getNum());
            Dialog dialog = new AlertDialog.Builder(getINSTANS().activity).setTitle("使用技能")
                    .setMessage("目标 " + slect + " 号玩家的身份是：" + woveskillViewModle.getPerpson(slect))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            //getINSTANS().eventPost(woveskillViewModle.getNum(),6,slect);

                        }
                    })
                    .show();
        } else putOut("未选择目标");
    }

    static void skillSw() {
        Dialog dialog = new AlertDialog.Builder(getINSTANS().activity).setTitle("使用技能")
                .setMessage("目标：" + slect + " 号玩家")
                .setPositiveButton("守护", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //getINSTANS().eventPost(woveskillViewModle.getNum(), 7, slect);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    static void skillLr() {

    }

    static void skillQs() {

    }

    static void skillWoves() {//狼人设置好各自的目标，由房主来统计操作结果并更改对应玩家状态
        Log.d(TAG, "skillWoves:in " + 9);
        Dialog dialog = new AlertDialog.Builder(getINSTANS().activity).setTitle("使用技能")
                .setMessage("目标：" + slect + " 号玩家")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //getINSTANS().eventPost(woveskillViewModle.getNum(),11,slect);
                        woveskillViewModle.getPlayer(woveskillViewModle.getNum()).getRole().setDest(slect);
                        getINSTANS().upSite(woveskillViewModle.getNum());
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    static void skillWhiteWoves() {

    }

    static void skillWovesKing() {

    }

    static void skillLmr() {

    }

    static Integer getkill() {
        HashMap<Integer, Integer> dests = new HashMap<>();
        int kill = 0;
        int times = 0;
        for (int i = 1; i < woveskillViewModle.configure.size() + 1; i++) {
            if (woveskillViewModle.getPlayer(i).getRole().getAbility() == 7) {
                int dest = woveskillViewModle.getPlayer(i).getRole().getDest();
                if (dest != 0) {
                    if (dests.containsKey(dest)) {
                        dests.put(dest, dests.get(dest) + 1);
                    } else dests.put(dest, 1);

                    if (dests.get(dest) > times) {
                        times = dests.get(dest);
                        kill = dest;
                    }
                }
            }
        }
        return kill;
    }

    public static void upkill() {
        int kill = getkill();
        if (kill==0){
            return;
        }
        woveskillViewModle.getPlayer(kill).getRole().setState(0);
        getINSTANS().upSite(kill);
    }

    void updateRoom(){
        room.setUsers(woveskillViewModle.users);
        Gson gson=new Gson();
        MyOkhttp.getINSTANCE().updateRoom(token,gson.toJson(room));
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                putOut("updateRoom faied!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    String res=response.body().string();
                Log.d(TAG, "onResponse: updateRoom"+res);
            }
        });
    }
    public void getResult(){
        Integer kill=getkill();
        Integer state=1;
        Integer sw=0,nv=0;
        deadList=new ArrayList<>();
        for (int i = 1; i <woveskillViewModle.configure.size() ; i++) {
           Integer num=woveskillViewModle.setRole(i).getNum();
           Integer skill=woveskillViewModle.setRole(i).getSkills();
           Integer dest=woveskillViewModle.setRole(i).getDest();
           if (num==3&&dest!=0){//女巫
               nv=dest;

           }else if (num==6&&dest!=0){//守卫
               sw=dest;
           }
        }
        if (kill!=0){//狼刀
            state=0;
        if (nv!=0&&nv.equals(kill)){//救人
            state=2;
        }
        if (nv!=0&&!nv.equals(kill)){//毒杀
            deadList.add(nv);
            woveskillViewModle.setRole(nv).setState(-1);
        }
        if (sw!=0&&sw.equals(kill)){
            if (state==2){//奶穿
                deadList.add(kill);
                woveskillViewModle.setRole(kill).setState(0);
            }else {//守中
               state=1;
            }
        }
        if (state==0){
            deadList.add(kill);
            woveskillViewModle.setRole(kill).setState(-1);
        }
        }else if (nv!=0){//毒杀
            deadList.add(nv);
            woveskillViewModle.setRole(nv).setState(-1);
        }
        updateRoom();
    }

}
