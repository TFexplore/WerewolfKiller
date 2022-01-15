package com.example.werewolfkiller.login;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.werewolfkiller.MyViewModle;
import com.example.werewolfkiller.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){//状态栏文字颜色随主题色变化
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
       MyViewModle myViewModle=new ViewModelProvider(this).get(MyViewModle.class);
        String integer="No";
        myViewModle.setRegistered(integer);
        Log.d(TAG, "onCreate: "+integer);
        navController = Navigation.findNavController(this, R.id.loginfragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

    }

}
