package com.example.werewolfkiller;

import com.example.werewolfkiller.activity.Activity;
import com.example.werewolfkiller.modle.User;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOkhttp{

    OkHttpClient okHttpClient;
    Request request;
    RequestBody requestBody;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    HttpUrl.Builder builder;
    Call call;
    public static MyOkhttp INSTANCE=new MyOkhttp();

    public MyOkhttp() {
        this.okHttpClient=new OkHttpClient();
    }

    public static MyOkhttp getINSTANCE() {
        return INSTANCE;
    }

    public Call getCall() {
        return call;
    }

    public void login(String passwd, String tel){
       requestBody=new FormBody.Builder()
                .add("Password",passwd)
                .add("Tel",tel)
                .build();
       request=new Request.Builder()
                .url("http://table.ethreal.cn/user/login")
                .post(requestBody)
                .build();
       call=new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post(new MessageEvent(-1," "));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                        String res=response.body().string();
                        Gson gson=new Gson();
                        Map<Object, Object> map=gson.fromJson(res, Map.class);
                        if ((Double)map.get("code")==200)
                        {
                            EventBus.getDefault().post(new MessageEvent(200,res));

                        }
                        else if ((Double)map.get("code")==500){
                            EventBus.getDefault().post(new MessageEvent(500,res));
                        }

            }
        });//call
    }
    public void register(String userName, String pwd, String tel){
        RequestBody requestBody = new FormBody.Builder()
                .add("uname", userName)
                .add("upassword", pwd)
                .add("utel", tel)
                .build();
        Request request = new Request.Builder()
                .url("http://table.ethreal.cn/user/register")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post(new MessageEvent(-1,""));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EventBus.getDefault().post(new MessageEvent(1,response.body().string()));
            }
        });

    }

    public void creatRoom(String body, String token){
        RequestBody requestBody = RequestBody.create(JSON, body);//设置请求body
        HttpUrl.Builder builder = HttpUrl.parse("http://table.ethreal.cn/rooms/newRoom").newBuilder();//url
        builder.addQueryParameter("rType", "1");//设置参数
        Request request = new Request.Builder()
                .url(builder.build())
                .addHeader("token", token)//设置头部token
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);//发起请求
    }

    public void getRoom(String inputConent, String token){
        HttpUrl.Builder builder = HttpUrl.parse("http://table.ethreal.cn/rooms/selectOne").newBuilder();//url
        builder.addQueryParameter("rId", inputConent);//设置参数
        Request request = new Request.Builder()
                .url(builder.build())
                .addHeader("token", token)//设置头部token
                .get()
                .build();
        call = okHttpClient.newCall(request);//发起请求
    }
    public  void  addPlayer(String player, String inputConent, String token){
        RequestBody requestBody = RequestBody.create(JSON, player);//设置请求body
        HttpUrl.Builder builder = HttpUrl.parse("http://table.ethreal.cn/rooms/addRoom").newBuilder();//url
        builder.addQueryParameter("rId", inputConent);//设置参数
        Request request = new Request.Builder()
                .url(builder.build())
                .addHeader("token", token)//设置头部token
                .post(requestBody)
                .build();
       call = okHttpClient.newCall(request);//发起请求


    }
    public void reStart(String site, String id, String token){
        RequestBody requestBody = RequestBody.create(JSON, site);//设置请求body
        HttpUrl.Builder builder = HttpUrl.parse("http://table.ethreal.cn/gameop/assignRoles").newBuilder();//url
        builder.addQueryParameter("roomId", id);//设置参数
        Request request = new Request.Builder()
                .url(builder.build())
                .addHeader("token", token)//设置头部token
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);//发起请求
    }
    public void dayNight(String inputConent, String token){
        requestBody=new FormBody.Builder()
                .add("roomId",inputConent)
                .build();
        Request request = new Request.Builder()
                .url("http://table.ethreal.cn/gameop/toNight")
                .addHeader("token", token)//设置头部token
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);//发起请求
    }
    public void daybegin(String inputConent, String token){
        requestBody=new FormBody.Builder()
                .add("roomId",inputConent)
                .build();
        Request request = new Request.Builder()
                .url("http://table.ethreal.cn/gameop/toDayNight")
                .addHeader("token", token)//设置头部token
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);//发起请求
    }
    public void event(String body, String id, String token){
        RequestBody requestBody = RequestBody.create(JSON, body);//设置请求body
        HttpUrl.Builder builder = HttpUrl.parse("http://table.ethreal.cn/gameop/addEvent").newBuilder();//url
        builder.addQueryParameter("roomId", id);//设置参数
        Request request = new Request.Builder()
                .url(builder.build())
                .addHeader("token", token)//设置头部token
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);//发起请求
    }//事件提交
    public void updataUser(User user, String token){
        RequestBody requestBody = new FormBody.Builder()
                .add("uage",""+user.getUage())
                .add("uname", user.getUname())
                .add("upersonality", user.getUpersonality())
                .add("uqq", user.getUqq())
                .add("usex",user.getUsex())
                .add("uwechat",user.getUwechat())
                .build();
        Request request = new Request.Builder()
                .url("http://table.ethreal.cn/user/update")
                .addHeader("token",token)
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);
    }//更新个人信息
    public void push(Activity activity, String token, String uid){

        Long l= System.currentTimeMillis();
        Date date=new Date(l);
        RequestBody requestBody = new FormBody.Builder()
                .add("content",activity.getContent())
                .add("latitude", ""+activity.getLatitude())
                .add("longitude", ""+activity.getLongitude())
                .add("plocate",activity.getLocal())
                .add("pnum", ""+activity.getPnum())
                .add("ptime",activity.getPtime())
                .add("themeid",""+activity.getThemeid())
                .add("title",activity.getTitle())
                .add("userid",""+uid)
                .build();
        Request request = new Request.Builder()
                .url("http://table.ethreal.cn/cityteams/newPost")
                .addHeader("token",token)
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);
    }
    public void getList(String offset, String num, String token){
        HttpUrl.Builder builder = HttpUrl.parse("http://table.ethreal.cn/cityteams/getList").newBuilder();//url
        builder.addQueryParameter("limit", num);//设置参数
        builder.addQueryParameter("offset",offset);
        Request request = new Request.Builder()
                .url(builder.build())
                .addHeader("token", token)//设置头部token
                .get()
                .build();
        call = okHttpClient.newCall(request);//发起请求
    }
    public void onVote(String id, String token){
        requestBody=new FormBody.Builder()
                .add("roomId",id)
                .build();
        request=new Request.Builder()
                .url("http://table.ethreal.cn/gameop/onVote")
                .addHeader("token",token)
                .post(requestBody)
                .build();
        call=new OkHttpClient().newCall(request);
    }//投票
    public void offVote(String id, String token){
        requestBody=new FormBody.Builder()
                .add("roomId",id)
                .build();
        request=new Request.Builder()
                .url("http://table.ethreal.cn/gameop/offVote")
                .addHeader("token",token)
                .post(requestBody)
                .build();
        call=new OkHttpClient().newCall(request);
    }
    public void updateRoom(String token, String room){
        requestBody=new FormBody.Builder()
                .add("room",room)
                .build();
        request=new Request.Builder()
                .url("http://table.ethreal.cn/rooms/replaceRoom")
                .addHeader("token",token)
                .post(requestBody)
                .build();
        call=new OkHttpClient().newCall(request);
    }


    public static interface OnMsgReturned{
        void onReturned(String msg);
        void onFaied();
    }


}
