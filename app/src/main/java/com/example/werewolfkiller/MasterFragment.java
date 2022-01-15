package com.example.werewolfkiller;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.werewolfkiller.databinding.FragmentMasterBinding;
import com.example.werewolfkiller.modle.User;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.lang.reflect.Field;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   MyViewModle myViewModle;

    private FragmentMasterBinding binding;
    NavigationMenuView navigationMenuView;

    public MasterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MasterFragment newInstance(String param1, String param2) {
        MasterFragment fragment = new MasterFragment();
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
        binding = FragmentMasterBinding.inflate(getLayoutInflater());
        MainActivity.setWindowStatusBarColor(getActivity(), getActivity().getResources().getColor(R.color.master));
        return binding.getRoot();//inflater.inflate(R.layout.fragment_master, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myViewModle =new ViewModelProvider(requireActivity()).get(MyViewModle.class);


        try {
            dataInit();
            init();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        HeaderLayoutInit();
        Toast.makeText(getContext(),new Gson().toJson(myViewModle.getUser()), Toast.LENGTH_SHORT).show();
    }
    void dataInit() throws NoSuchFieldException, IllegalAccessException {
        User user;
        user=myViewModle.getUser();
        Class<?> d = R.mipmap.class;
        Field imgId = null;
        imgId = d.getField("icon_" + user.getUavatar());
        binding.drawer.setImageResource(imgId.getInt(requireActivity()));
        binding.nameMaster.setText(user.getUname());
    }
    void  HeaderLayoutInit(){
        View view=binding.navigationView.inflateHeaderView(R.layout.headerlayout);
        View viewbk=view.findViewById(R.id.header_bk);
        ImageView avatar=(ImageView) view.findViewById(R.id.avatar);
        Class<?> c= R.mipmap.class;
        Field idfield= null;
        try {
            idfield = c.getField("icon_"+myViewModle.getUser().getUavatar());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        int avatarId= 0;//头像
        try {
            avatarId = idfield.getInt(requireActivity());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        avatar.setImageResource(avatarId);
        avatar.setOnClickListener(v->{
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_masterFragment_to_personFragment);
            Log.d(TAG, "HeaderLayoutInit: 1111111111k");
        });

        TextView textView=(TextView) view.findViewById(R.id.name_header);
        textView.setText(myViewModle.getUser().getUname());
    }

    private void init() throws NoSuchFieldException, IllegalAccessException {
        navigationMenuView = (NavigationMenuView) binding.navigationView.getChildAt(0);
        binding.masterNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_masterFragment_to_listFragment);
            }
        });
        binding.masterCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_masterFragment_to_configureFragment);
            }
        });
        binding.masterEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_masterFragment_to_enterFragment);
            }
        });
        binding.drawer.setOnClickListener(new View.OnClickListener() {//菜单展开
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
        // NavigationView 监听



        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.group_item_github:
                        Toast.makeText(requireActivity(), "项目主页", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.group_item_more:
                        Toast.makeText(requireActivity(), "更多内容", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.group_item_qr_code:
                        Toast.makeText(requireActivity(), "二维码", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.group_item_share_project:
                        Toast.makeText(requireActivity(), "分享项目", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_model:
                        Toast.makeText(requireActivity(), "夜间模式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_about:
                        Toast.makeText(requireActivity(), "关于", Toast.LENGTH_SHORT).show();
                        break;
                }
                item.setCheckable(false);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }
}