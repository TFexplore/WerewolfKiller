package com.example.werewolfkiller.woveskill;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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
import com.example.werewolfkiller.databinding.FragmentConfigureBinding;
import com.example.werewolfkiller.woveskill.data.Player;
import com.example.werewolfkiller.woveskill.operate.Operator;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigureFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentConfigureBinding binding;
    WoveskillViewModle woveskillViewModle;
    RecyclerAd myAdapter;
    RecyclerView recyclerView;
    MyViewModle myViewModle;


    public ConfigureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigureFragment newInstance(String param1, String param2) {
        ConfigureFragment fragment = new ConfigureFragment();
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
        MainActivity.setWindowStatusBarColor(getActivity(), getActivity().getResources().getColor(R.color.configure));
        binding = FragmentConfigureBinding.inflate(inflater);
        return binding.getRoot();//inflater.inflate(R.layout.fragment_configure, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        woveskillViewModle = new ViewModelProvider(requireActivity()).get(WoveskillViewModle.class);
        myViewModle = new ViewModelProvider(requireActivity()).get(MyViewModle.class);
        woveskillViewModle.getUserNum().observe(getViewLifecycleOwner(), userNum -> {
            binding.userNum.setText("总人数：" + userNum + " 人");
        });
        myAdapter = new RecyclerAd(requireActivity(), woveskillViewModle);
        recyclerView = getView().findViewById(R.id.recyclerView);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(myAdapter);
        init();
        binding.back.setOnClickListener(v->{
            NavController navController= Navigation.findNavController(v);
            navController.navigate(R.id.action_configureFragment_to_cellFragment);
        });

    }

    public void init() {
        Operator.getINSTANS().setActivity(requireActivity());
        binding.compelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.compelete.setBackgroundColor(getResources().getColor(R.color.cell));
                myAdapter.loadConfigure();//读取configure
                Gson gson = new Gson();
                Player master = new Player(myViewModle.getUser().getUname(), "" + myViewModle.getUser().getUavatar(), 0);//房主信息
                master.setUid(myViewModle.getUser().getUid());//设置uid
                Map<String, Object> map = new HashMap<>();//消息载体
                map.put("configure", woveskillViewModle.getConfigure());
                map.put("locate_role", woveskillViewModle.getLocate_role());
                map.put("player", master);
                woveskillViewModle.setMaster(true);
                Operator.getINSTANS().createRoom(gson.toJson(map),myViewModle.getToken());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
            if (event.getCode() == 1) {
                Operator.Redeal();
            NavController controller = Navigation.findNavController(binding.compelete);
            controller.navigate(R.id.action_configureFragment_to_cellFragment);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onPause(){
        super.onPause();
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onPause: ");
    }

}