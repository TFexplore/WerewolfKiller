package com.example.werewolfkiller.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.werewolfkiller.MainActivity;
import com.example.werewolfkiller.MessageEvent;
import com.example.werewolfkiller.MyOkhttp;
import com.example.werewolfkiller.MyViewModle;
import com.example.werewolfkiller.R;
import com.example.werewolfkiller.databinding.FragmentNewLoginBinding;
import com.example.werewolfkiller.modle.User;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newLoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MyViewModle myViewModle;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentNewLoginBinding binding;

    private Tencent mTencent;
    private static final String APPID = "101988302";
    private QQLoginListener mListener;
    private UserInfo mInfo;
    private String name, figureurl;

    public newLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static newLoginFragment newInstance(String param1, String param2) {
        newLoginFragment fragment = new newLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentNewLoginBinding.inflate(inflater);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myViewModle=new ViewModelProvider(requireActivity()).get(MyViewModle.class);

        binding.toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_newLoginFragment_to_registerFragment);
            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=new User();
                String pwd=binding.pwd.getText().toString();
                String tel=binding.user.getText().toString();
                user.setUtel(tel);
                user.setUpassword(pwd);
                myViewModle.setUser(user);
                MyOkhttp.getINSTANCE().login(pwd,tel);

            }
        });
        //
        mListener = new QQLoginListener();
        // 实例化Tencent
        if (mTencent == null) {
            mTencent = Tencent.createInstance(APPID, requireContext());
        }

        // qq登录

        binding.loginQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQLogin();
            }
        });
        //微信登录
        binding.loginWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /**
     * 登录方法
     */
    private void QQLogin() {
        //如果session不可用，则登录，否则说明已经登录
        if (!mTencent.isSessionValid()) {
            mTencent.login(requireActivity(), "get_simple_userinfo", mListener);
        }
    }

    // 实现登录成功与否的接口
    private class QQLoginListener implements IUiListener {

        @Override
        public void onComplete(Object object) { //登录成功
            //获取openid和token
            initOpenIdAndToken(object);
            //获取用户信息
            getUserInfo();
        }

        @Override
        public void onError(UiError uiError) {  //登录失败
        }

        @Override
        public void onCancel() {                //取消登录
        }

        @Override
        public void onWarning(int i) {

        }
    }

    private void initOpenIdAndToken(Object object) {
        JSONObject jb = (JSONObject) object;
        try {
            String openID = jb.getString("openid");  //openid用户唯一标识
            String access_token = jb.getString("access_token");
            String expires = jb.getString("expires_in");

            mTencent.setOpenId(openID);
            mTencent.setAccessToken(access_token, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {//腾讯api获取用户信息
        QQToken token = mTencent.getQQToken();
        mInfo = new UserInfo(requireContext(), token);
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object object) {
                JSONObject jb = (JSONObject) object;
                try {
                    Log.d(TAG, "onComplete: "+jb.toString());
                    name = jb.getString("nickname");
                    figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
                    /*Log.i("imgUrl",figureurl.toString()+"");*/
                   // binding.loginName.setText(name);
                    /*Glide.with(MainActivity.this).load(figureurl).into(figure);*/
                    Uri parse = Uri.parse(figureurl);
                    //figure.setImageURI(parse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onWarning(int i) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//腾讯api
        mTencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
                if (event.getCode()==-1){
                    Toast.makeText(getContext(),"请求失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                }
                else if (event.getCode()==200){
                    Toast.makeText(getContext(),event.getRes(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "run: "+event.getRes());
                    binding.login.setText("登录成功，正在跳转...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(requireActivity(), MainActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("msg",event.getRes());
                            mainIntent.putExtra("bundle",bundle);
                            requireActivity().startActivity(mainIntent);
                            requireActivity().finish();
                        }
                    }, 2000);
                }
                else if (event.getCode()==500){
                    Gson gson=new Gson();
                    Map<Object, Object> map=gson.fromJson(event.getRes(), Map.class);
                    Toast.makeText(getContext(),(String)map.get("msg"), Toast.LENGTH_SHORT).show();
                }
            }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}