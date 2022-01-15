package com.example.werewolfkiller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.werewolfkiller.databinding.ActivitySplashBinding;
import com.example.werewolfkiller.login.LoginActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "dddddd";//启动图页面
    private int isFisrst;
    private ActivitySplashBinding binding;
    String tel;
    String token;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        load();
        if (tel.equals("")) {
            TO(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    HttpUrl.Builder builder = HttpUrl.parse("http://table.ethreal.cn/user/select").newBuilder();
                    builder.addQueryParameter("utel", tel);
                    Request request = new Request.Builder()
                            .url(builder.build())
                            .addHeader("token", token)
                            .get()
                            .build();
                    Log.d("get", "run: " + request.toString());
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("tag", "onFailure: " + call.toString() + ":  " + e.getMessage().toString());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplication(), "请求失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            Gson gson = new Gson();
                            Map<Object, Object> map = gson.fromJson(res, Map.class);
                            Log.d("tag", "onResponse: " + res);
                            if (map.containsKey("code")) {
                                Log.d("tag", "onResponse: " + "containes");
                                if ((Double) map.get("code") == 200) {

                                    Intent mainIntent = new Intent(getApplication(), MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("msg", res);
                                    mainIntent.putExtra("bundle", bundle);
                                    activity.startActivity(mainIntent);
                                    activity.finish();
                                } else if ((Double) map.get("code") == 500) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("faied", "run: " + tel + token);
                                            Toast.makeText(getApplication(), (String) map.get("msg"), Toast.LENGTH_SHORT).show();
                                            TO(true);
                                        }
                                    });

                                }
                                return;
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplication(),"登录已过期", Toast.LENGTH_SHORT).show();
                                    TO(true);
                                }
                            });


                        }
                    });

                }
            }, 500);

        }


    }

    void load() {
        Log.d(TAG, "load: "+1);
        SharedPreferences shp = getSharedPreferences("my_data", Context.MODE_PRIVATE);
        tel = shp.getString("tel", "");
        token = shp.getString("token", "");
        if (tel.equals("")){
            TO(true);
        }
        Log.d("TAG", "load: " + tel + token);
    }

    void TO(boolean flag) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {//

                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                   SplashActivity.this.startActivity(mainIntent);
                   SplashActivity.this.finish();
                }
            }
        }, 500);
    }

}

