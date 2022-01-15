//edit by zhang
package com.example.werewolfkiller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.werewolfkiller.modle.User;
import com.example.werewolfkiller.woveskill.WoveskillViewModle;
import com.google.gson.Gson;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";
    NavController navController;
    private long pressdTime;
    private Activity activity;
    private MyViewModle myViewModle;
    //private ActivityMainBinding binding;
    WoveskillViewModle woveskillViewModle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        // binding=ActivityMainBinding.inflate(getLayoutInflater());
        //binding.getRoot();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){//状态栏文字颜色随主题色变化
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);
        myViewModle = ViewModelProviders.of(this).get(MyViewModle.class);
        woveskillViewModle=new ViewModelProvider(this).get(WoveskillViewModle.class);
        activity = this;
        init();
        dataInit();
       /* SharedPreferences shp = getSharedPreferences("my_data", Context.MODE_PRIVATE);
        if (shp.contains("user")) {
            User user;
            user = new Gson().fromJson(shp.getString("user", ""), User.class);
            //myViewModle.setUser(user);
        } else {
            //navController.navigate(R.id.action_masterFragment_to_loginFragment);
        }

        */


    }
    private void dataInit(){

        Intent intent=this.getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");
        String rMsg=bundle.getString("msg");
        Gson gson=new Gson();
        Map<Object, Object> map=gson.fromJson(rMsg, Map.class);

        Map<Object, Object> data=(Map<Object, Object>)map.get("data");
        Log.d(TAG, "dataInit: "+gson.toJson(data.get("user")));
        User user=gson.fromJson(gson.toJson(data.get("user")),User.class);
        myViewModle.setUser(user);
        myViewModle.getUser().setUavatar("4");
        SharedPreferences shp=this.getSharedPreferences("my_data", Context.MODE_PRIVATE);
        if (data.containsKey("token")){
            myViewModle.setToken((String) data.get("token"));

            SharedPreferences.Editor editor=shp.edit();
            editor.putString("tel",user.getUtel());
            editor.putString("token",(String) data.get("token"));
            editor.apply();
        }
        myViewModle.setToken(shp.getString("token",""));
    }

    private void init() {
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                // Log.d("TAG", "onDestinationChanged: ---------"+destination.getId());
                switch (destination.getId()) {
                    case R.id.masterFragment:
                        Log.d(TAG, "onDestinationChanged: master");

                        break;
                    //case R.id.loginFragment:
                       // Log.d(TAG, "onDestinationChanged: login");

                        //break;
                    case R.id.enterFragment:
                        Log.d(TAG, "onDestinationChanged: enter");

                        break;
                    case R.id.cellFragment:
                        Log.d(TAG, "onDestinationChanged: cell");

                        break;
                    case R.id.configureFragment:
                        Log.d(TAG, "onDestinationChanged: configure");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                //顶部状态栏
                window.setStatusBarColor(colorResId);

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        long pressTime = System.currentTimeMillis();
        Log.d(TAG, "onBackPressed: main:" + pressdTime + "  " + pressTime);
        switch (navController.getCurrentDestination().getId()) {
            case R.id.masterFragment:
                if (pressTime - pressdTime > 800) {
                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    pressdTime = pressTime;

                } else System.exit(0);
                break;

            case R.id.cellFragment:
                if (pressTime - pressdTime > 800) {
                    Toast.makeText(MainActivity.this, "再按一次退出房间", Toast.LENGTH_SHORT).show();
                    pressdTime = pressTime;
                } else {

                    navController.navigate(R.id.action_cellFragment_to_masterFragment);
                }
                break;
            default:
                super.onBackPressed();
                break;
        }

    }


}