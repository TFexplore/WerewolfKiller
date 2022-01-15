package com.example.werewolfkiller.login;

import android.content.Intent;
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
import com.example.werewolfkiller.databinding.FragmentRegisterBinding;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRegisterBinding binding;
    private Handler handler;
    Integer tem=0;

    MyViewModle myViewModle;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myViewModle = new ViewModelProvider(requireActivity()).get(MyViewModle.class);

        binding.toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_registerFragment_to_newLoginFragment);
            }
        });
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tem==0){
                    Toast.makeText(requireActivity().getApplicationContext(),"请选择头像", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userName = binding.userName.getText().toString();
                String pwd = binding.pwd.getText().toString();
                String tel = binding.user.getText().toString();
                MyOkhttp.getINSTANCE().register(userName, pwd, tel);

            }
        });
        binding.avatarChose.setOnClickListener(v->{
            try {
                Random random=new Random();

            int r=0;

            r=random.nextInt(12)+1;
            tem=r;
            if (r==tem){
                r=random.nextInt(12)+1;
            }
            Class<?> d = R.mipmap.class;
            Field imgId = null;
            imgId = d.getField("icon_" + r);
            binding.avatarChose.setImageResource(imgId.getInt(requireActivity()));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        });//头像选中
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        if (event.getCode()==-1){
            Toast.makeText(getContext(),"请求失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
        }
        else if (event.getCode()==1){
            Gson gson = new Gson();
            String res =event.getRes();
            Map<Object, Object> map = gson.fromJson(res, Map.class);

            if ((Double)map.get("code")==200) {
                Log.d(TAG, "run: " + gson.toJson(map));
                binding.register.setText("注册成功，正在跳转登录...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent mainIntent = new Intent(requireActivity(), MainActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("msg",res);
                        mainIntent.putExtra("bundle",bundle);
                        requireActivity().startActivity(mainIntent);
                        requireActivity().finish();
                    }
                }).start();
            } else if ((Double) map.get("code") == 500) {
                Toast.makeText(getContext(), (String) map.get("msg"), Toast.LENGTH_SHORT).show();
            }
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