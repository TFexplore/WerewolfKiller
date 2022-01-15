package com.example.werewolfkiller.woveskill;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.werewolfkiller.MainActivity;
import com.example.werewolfkiller.MessageEvent;
import com.example.werewolfkiller.MyViewModle;
import com.example.werewolfkiller.R;
import com.example.werewolfkiller.databinding.FragmentCellBinding;
import com.example.werewolfkiller.modle.Message;
import com.example.werewolfkiller.woveskill.data.Mould;
import com.example.werewolfkiller.woveskill.operate.Operator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CellFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MessageRecyclerAd myAdapter;
    RecyclerView recyclerView;

    private FragmentCellBinding binding;
    private int isexpand = 0;
    private int select = 0;

    WoveskillViewModle woveskillViewModle;
    MyViewModle myViewModle;

    MediaPlayer player;
    Game game;

    boolean flag_vote = false;

    public CellFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CellFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CellFragment newInstance(String param1, String param2) {
        CellFragment fragment = new CellFragment();
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
        binding = FragmentCellBinding.inflate(getLayoutInflater());
        MainActivity.setWindowStatusBarColor(getActivity(), getActivity().getResources().getColor(R.color.cell));
        return binding.getRoot();//inflater.inflate(R.layout.fragment_cell, container, false);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        woveskillViewModle = new ViewModelProvider(requireActivity()).get(WoveskillViewModle.class);
        myViewModle = new ViewModelProvider(requireActivity()).get(MyViewModle.class);
        Log.d(TAG, "onActivityCreated: " + myViewModle.getToken());
        myAdapter = new MessageRecyclerAd(requireActivity());
        recyclerView = binding.recyl;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(myAdapter);

        Operator.getINSTANS().setActivity(requireActivity());
        Operator.setSlect(0);
        onMasterTouch_init();
        try {
            userImag_inti();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            sitesInit();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        onUserTouch_init();
        binding.roomNo.setText(String.valueOf(woveskillViewModle.getKey_Room()));
    }

    void sitesInit() throws NoSuchFieldException, IllegalAccessException {
        updataPlayer();
        woveskillViewModle.userIn.observe(getViewLifecycleOwner(), userIn -> {
            binding.users.setText(new String("" + userIn));
        });
    }//ob

    void onUserTouch_init() {
        binding.febMore.setOnClickListener(v -> {//More
            // Log.d(TAG, "onUserTouch_init: more");
            if (woveskillViewModle.users.size() < 12) {
                Random r = new Random();
                while (true) {
                    int i = r.nextInt(12) + 1;
                    if (!woveskillViewModle.contained(i)) {
                        woveskillViewModle.addUser(i, Mould.getMould().getUser(i - 1));
                        myAdapter.messages.add(new Message(Mould.getMould().getUser(i - 1).getName(), "000001", "进入房间"));
                        myAdapter.notifyDataSetChanged();//刷新
                        recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                        Operator.getINSTANS().addTest(i);
                       /* try {
                            userAdd(Mould.getMould().getUser(i - 1));

                        } catch (NoSuchFieldException | java.lang.InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        */
                        break;
                    }
                }
            }

        });
        binding.febSkill.setOnClickListener(v -> {//使用技能
            Log.d(TAG, "onUserTouch_init: skill");
            Operator.getINSTANS().getRoom(1005);
            // MyOkhttp.getINSTANCE().getRoom("" + woveskillViewModle.getKey_Room(), myViewModle.getToken(), 5);
        });
        binding.febCheck.setOnClickListener(v -> {//查看身份
            Log.d(TAG, "onUserTouch_init: check");
            Operator.getINSTANS().getRoom(1002);
        });
        binding.febToupiao.setOnClickListener(v -> {//投票
            Log.d(TAG, "onUserTouch_init: toupiao");
            if (select == 0) {
                Toast.makeText(requireActivity().getApplicationContext(), "请选择目标", Toast.LENGTH_SHORT).show();
            }
            woveskillViewModle.getPlayer(woveskillViewModle.getNum()).getRole().setDest(select);
            Operator.getINSTANS().upSite(woveskillViewModle.getNum());
        });
        //返回键
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("是否退出");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavController controller = Navigation.findNavController(requireView());
                        controller.navigate(R.id.action_cellFragment_to_masterFragment);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
        //菜单
        binding.feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//菜单图标
                if (binding.febMenu2.isExpanded()) {
                    binding.febMenu2.collapse();
                    return;
                }
                if (binding.febMenu1.isExpanded()) {
                    binding.febMenu1.collapse();
                    binding.febMenu2.expand();
                } else if (woveskillViewModle.isMaster()) {
                    binding.febMenu1.expand();
                } else binding.febMenu2.expand();
            }
        });
    }

    void onMasterTouch_init() {
        game = new Game(requireActivity(), woveskillViewModle, woveskillViewModle.getConfigure());
        if (game.getFlag() < 0) {
            game.start();
        }
        binding.febToupiaoMaster.setOnClickListener(v -> {//发起投票
            Operator.getINSTANS().onVote();
            flag_vote = true;
        });
        binding.febDaynight.setOnClickListener(new View.OnClickListener() {//进入天黑
            @Override
            public void onClick(View v) {
                if (game.getFlag() == 0)
                    LockSupport.unpark(game);
                Operator.getINSTANS().dayNight();
            }
        });//进入天黑
        binding.febRefapai.setOnClickListener(v -> {//重新发牌
            Operator.Redeal();
        });//重新发牌
        binding.febPush.setOnClickListener(v -> {//公布结果
            if (flag_vote) {//投票结果
                Operator.getINSTANS().getRoom(1004);
                flag_vote = false;

            }else if (Operator.getINSTANS().data>0){//死亡结果
                myAdapter.messages.add(Operator.getINSTANS().deadMsg);
                myAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
            }
        });//公布结果
    }

    void userImag_inti() throws IllegalAccessException, NoSuchFieldException {

        for (int i = 0; i < 12; i++) {
            Class<?> c = R.id.class;
            Field idfield = c.getField("user" + (i + 1));
            ImageView imageAvatar = binding.getRoot().findViewById(idfield.getInt(requireActivity()));
            int num = i + 1;
            imageAvatar.setOnClickListener(v -> {
                if (woveskillViewModle.num==0) {
                    Log.d(TAG, "onClick: 2");
                    Operator.getINSTANS().slectSite(num);
                    return;
                }
                try {
                    if (select != 0) {

                        Class<?> d = R.id.class;
                        Field imgId = null;
                        imgId = d.getField("num" + select);

                        TextView numText = null;

                        numText = binding.getRoot().findViewById(imgId.getInt(requireActivity()));
                        numText.setTextColor(Color.parseColor("#FFFFFF"));

                    }
                    if (select != num) {
                        Log.d(TAG, "onClick: -----------" + select + " " + num);
                        select = num;
                        Operator.setSlect(num);
                        Class<?> d = R.id.class;
                        Field imgId = null;
                        imgId = d.getField("num" + select);

                        TextView numText = null;

                        numText = binding.getRoot().findViewById(imgId.getInt(requireActivity()));
                        numText.setTextColor(Color.parseColor("#ca393b"));
                    } else {
                        select = 0;
                        Operator.setSlect(0);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

            });
        }//设置选中，入座事件
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) throws NoSuchFieldException, IllegalAccessException {
        switch (event.getCode()) {
            case 0:
                break;
            case 1://选择位置
                updataPlayer();
                break;
            case 1002://死亡结果
                break;
            case 1003://投票结果
                myAdapter.messages.add(Operator.getINSTANS().voteMsg);
                myAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                break;

            case 1006://查看所有人身份
                int num = woveskillViewModle.configure.size();
                StringBuilder string = new StringBuilder();
                for (int i = 1; i < num + 1; i++) {
                    string.append(woveskillViewModle.getRole(i).getName()).append(" ");
                }
                myAdapter.messages.add(new Message("房间配置", "000001", string.toString()));
                myAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                break;
            case 5:
                Operator.Skills();
                break;
            case 1010:
                if (woveskillViewModle.contained(select)){
                    updataPlayer();
                    Operator.putOut("请重新选择");
                }else Operator.getINSTANS().slectSite(select);
                break;
            default:
                break;
        }
    }

    void updataPlayer() throws IllegalAccessException, NoSuchFieldException {
        for (int i = 1; i < 13; i++) {//初始化
            Class<?> d = R.id.class;
            Field field = d.getField("name" + i);
            TextView textView = binding.getRoot().findViewById(field.getInt(requireActivity()));
            textView.setText("");

            Field imgId = d.getField("user" + i);
            ImageView imageView = binding.getRoot().findViewById(imgId.getInt(requireActivity()));
            imageView.setImageResource(R.mipmap.icon_add);

            select = 0;
        }
        woveskillViewModle.userIn.setValue(woveskillViewModle.users.size());
        woveskillViewModle.users.forEach((key, player) -> {
            try {
                Class<?> d = R.id.class;
                Field field = d.getField("name" + player.getNum());
                TextView textView = binding.getRoot().findViewById(field.getInt(requireActivity()));
                textView.setText(player.getName());

                Class<?> c = R.mipmap.class;
                Field idfield = c.getField("icon_" + player.getAvatar());
                int avatar = idfield.getInt(requireActivity());//头像
                Field imgId = d.getField("user" + player.getNum());
                ImageView imageView = null;

                imageView = binding.getRoot().findViewById(imgId.getInt(requireActivity()));
                imageView.setImageResource(avatar);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

        });
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


}