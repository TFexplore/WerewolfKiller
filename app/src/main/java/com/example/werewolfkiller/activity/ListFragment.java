package com.example.werewolfkiller.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.example.werewolfkiller.MainActivity;
import com.example.werewolfkiller.MessageEvent;
import com.example.werewolfkiller.MyOkhttp;
import com.example.werewolfkiller.MyViewModle;
import com.example.werewolfkiller.R;
import com.example.werewolfkiller.activity.poisearch.Utils;
import com.example.werewolfkiller.databinding.FragmentListBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentListBinding binding;
    PopupWindow popupWindow_type;
    PopupWindow popupWindow_time;
    boolean isexpandType=false;
    boolean isexpandTime=false;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        binding= FragmentListBinding.inflate(inflater);
        MainActivity.setWindowStatusBarColor(getActivity(), getActivity().getResources().getColor(R.color.master));
        return binding.getRoot();//inflater.inflate(R.layout.fragment_list, container, false);
    }
    MyViewModle myViewModle;
    List<Activity> activities;
    RecylAdpter adpter;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private boolean isPermissionRequested;
    LatLng mlatLng;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myViewModle=new ViewModelProvider(requireActivity()).get(MyViewModle.class);
        requestPermission();
        RecyclerView recyclerView=binding.recylerActivity;
        adpter=new RecylAdpter(requireActivity());
        mapInit();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adpter);
        try {
            typeSlectorInit();
            timeSlectorInit();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        popWindowInit();
        binding.editImage.setOnClickListener(v->{
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_listFragment_to_editFragment);
        });
        dataGet();

        binding.back.setOnClickListener(v->{
            NavController navController= Navigation.findNavController(v);
            navController.navigate(R.id.masterFragment);
        });


    }
    void mapInit(){
        mLocationClient = new LocationClient(requireActivity().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
//可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求

    }
    void dataGet(){
        adpter.onMsgReturned=new RecylAdpter.OnMsgReturned() {
            @Override
            public void onReturned(String msg) {
                Bundle bundle=new Bundle();
                bundle.putString("res",msg);
                NavController controller = Navigation.findNavController(requireView());
                controller.navigate(R.id.action_listFragment_to_detailFragment,bundle);
            }

            @Override
            public void onFaied() {

            }
        };

        MyOkhttp.getINSTANCE().getList(""+0,""+10,myViewModle.getToken());
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.myToast(requireActivity(),"请检查网络后重试");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();
                Map<Object, Object> map = gson.fromJson(res, Map.class);
                if (!map.containsKey("code")) return;
                if ((Double) map.get("code") == 200) {
                 Utils.myToast(requireActivity(),"success");
                    Log.d(TAG, "onResponse: "+map.get("data"));
                 Map<Object, Object> data =  gson.fromJson(gson.toJson(map.get("data")), Map.class);
                 Type type=new TypeToken<List<Activity>>(){}.getType();
                 activities=gson.fromJson(gson.toJson(data.get("list")),type);
                    Log.d(TAG, "onResponse: "+gson.toJson(activities));
                 adpter.setActivityList(activities);

                 requireActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         adpter.notifyDataSetChanged();

                     }
                 });
                } else if ((Double) map.get("code") == 500) {
                    Utils.myToast(requireActivity(),"faied");
                }
            }
        });
    }
    void popWindowInit(){
        Drawable down= ContextCompat.getDrawable(requireContext(),R.drawable.icon_down);
        down.setBounds(0,0,25,25);
        Drawable up= ContextCompat.getDrawable(requireContext(),R.drawable.icon_up);
        up.setBounds(0,0,25,25);

        binding.time.setCompoundDrawables(null,null,up,null);
        binding.type.setCompoundDrawables(null,null,up,null);

        binding.time.setOnClickListener(v -> {

            popupWindow_time.setOutsideTouchable(true);
            popupWindow_time.setFocusable(true);
            popupWindow_time.showAsDropDown(binding.type);


            binding.time.setCompoundDrawables(null,null,down,null);
        });
        binding.type.setOnClickListener(v->{
            popupWindow_type.setOutsideTouchable(true);
            popupWindow_type.setFocusable(true);
            popupWindow_type.showAsDropDown(v);

            binding.type.setCompoundDrawables(null,null,down,null);
        });
        popupWindow_time.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.time.setCompoundDrawables(null,null,up,null);
            }
        });
        popupWindow_type.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.type.setCompoundDrawables(null,null,up,null);
            }
        });
    }
    void typeSlectorInit() throws NoSuchFieldException, IllegalAccessException {
        //绑定布局，设置控件点击事件
        View popup_view=requireActivity().getLayoutInflater().inflate(R.layout.type_pop,null);
        for (int i = 1; i <=3; i++) {
            Class<?> d=R.id.class;
            Field field=d.getField("type"+i);
            TextView type=popup_view.findViewById(field.getInt(requireActivity()));
            int finalI = i;
            type.setOnClickListener(v->{
                Log.d(TAG, "typeSlector: "+ finalI);
                switch (finalI){
                    case 1:
                        binding.type.setText("狼人杀");
                        break;
                    case 2:
                        binding.type.setText("剧本杀");
                        break;
                    case 3:
                        binding.type.setText("密  室");
                        break;
                    default:
                        break;
                }
                isexpandType=!isexpandType;
                popupWindow_type.dismiss();
            });
        }

        popupWindow_type =new PopupWindow(popup_view,binding.type.getMaxWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);

    }
    void timeSlectorInit() throws NoSuchFieldException, IllegalAccessException {
        Long l= System.currentTimeMillis();
        Date date=new Date(l);
        SimpleDateFormat wkFormat=new SimpleDateFormat("EEEE", Locale.CHINA);
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM-dd", Locale.CHINA);
        Calendar calendar= Calendar.getInstance();


        View popup_view=requireActivity().getLayoutInflater().inflate(R.layout.time_pop,null);//获取视图

        for (int i=1;i<=7;i++){//反射通过字符串获取id，设置日期
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_WEEK,i-1);
            String wk=wkFormat.format(calendar.getTime());
            String day=dateFormat.format(calendar.getTime());
            Log.d(TAG, "timeSlectorInit: "+wk+" "+day);
            Class<?> w=R.id.class;
            Field wfiled=w.getField("time"+i);
            TextView wk_view=popup_view.findViewById(wfiled.getInt(requireActivity()));
            wk_view.setText(wk);

            Class<?> d=R.id.class;
            Field day_field=d.getField("day"+i);
            TextView day_view=popup_view.findViewById(day_field.getInt(requireActivity()));
            day_view.setText(day);

            int finalI = i;
            day_view.setOnClickListener(v->{
                Log.d(TAG, "timeSlectorInit: "+ finalI);
            });
            wk_view.setOnClickListener(v->{
                Log.d(TAG, "timeSlectorInit: "+ finalI);
            });

        }
        popupWindow_time =new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        if (event.getCode()==1){
            Bundle bundle=new Bundle();
            bundle.putString("res",event.getRes());
            NavController controller = Navigation.findNavController(requireView());
            controller.navigate(R.id.action_listFragment_to_detailFragment,bundle);
        }

    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause(){
        super.onPause();

    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            mlatLng=new LatLng(latitude,longitude);
            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = location.getLocType();
            Log.d(TAG, "onReceiveLocation: ");
            adpter.setLatLng(mlatLng);
            Log.d(TAG, "onReceiveLocation: "+mlatLng.latitude+" "+mlatLng.longitude);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adpter.notifyDataSetChanged();
                }
            });
            mLocationClient.stop();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        }
    }
    /**
     * Android6.0之后需要动态申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {
            isPermissionRequested = true;
            ArrayList<String> permissionsList = new ArrayList<>();
            String[] permissions = {
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != requireActivity().checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                    // 进入到这里代表没有权限.
                }
            }

            if (!permissionsList.isEmpty()) {
                String[] strings = new String[permissionsList.size()];
                requestPermissions(permissionsList.toArray(strings), 0);
            }
        }
    }

}