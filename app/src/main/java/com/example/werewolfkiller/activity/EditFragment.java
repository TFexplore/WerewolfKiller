package com.example.werewolfkiller.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.werewolfkiller.MyOkhttp;
import com.example.werewolfkiller.MyViewModle;
import com.example.werewolfkiller.R;
import com.example.werewolfkiller.activity.poisearch.PoiCitySearchActivity;
import com.example.werewolfkiller.databinding.FragmentEditBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isPermissionRequested;
    FragmentEditBinding binding;
    MyViewModle myViewModle;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
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
        binding = FragmentEditBinding.inflate(inflater);
        return binding.getRoot();//inflater.inflate(R.layout.fragment_edit, container, false);
    }

    PopupWindow popupWindow_type;
    PopupWindow popupWindow_time;
    Boolean flag=false;
    Activity activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(requireActivity().getApplicationContext(), "open map", Toast.LENGTH_SHORT).show();
        binding.contextEdit.setGravity(Gravity.TOP);
        requestPermission();
        myViewModle = new ViewModelProvider(requireActivity()).get(MyViewModle.class);
        activity = new Activity();

        try {
            onTouchInit();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        binding.back.setOnClickListener(v->{
            NavController navController= Navigation.findNavController(v);
            navController.navigate(R.id.listFragment);
        });
        binding.location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>12){
                    binding.location.setTextSize(14);
                }
                else {
                    binding.location.setTextSize(18);
                }
            }
        });

    }

    void data_load() {
        if (binding.contextEdit.getText()==null){
            Toast.makeText(requireActivity().getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!flag){
            Toast.makeText(requireActivity().getApplicationContext(), "请选择位置", Toast.LENGTH_SHORT).show();
            binding.dingwei.callOnClick();
            return;
        }
        activity.setContent(binding.contextEdit.getText().toString());
        activity.setTitle(binding.title.getText().toString());
        activity.setLocal(binding.location.getText().toString());
        activity.setPnum(Integer.valueOf(binding.num.getText().toString()));
        Log.d(TAG, "data_load: " + new Gson().toJson(activity));

        MyOkhttp.getINSTANCE().push(activity, myViewModle.getToken(), "" + myViewModle.getUser().getUid());
        MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireActivity().getApplicationContext(), "请检查网络后重试", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                Gson gson = new Gson();

                Map<Object, Object> map = gson.fromJson(res, Map.class);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ((Double) map.get("code") == 200) {
                            Toast.makeText(requireActivity().getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            NavController controller=Navigation.findNavController(binding.back);
                            controller.navigate(R.id.action_editFragment_to_listFragment);
                        } else if ((Double) map.get("code") == 500) {
                            Toast.makeText(requireActivity().getApplicationContext(), "faied", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }

    void onTouchInit() throws NoSuchFieldException, IllegalAccessException {
        binding.complete.setOnClickListener(v -> {
            data_load();
        });//发布
        binding.textComplete.setOnClickListener(v -> {
            data_load();
        });
        binding.back.setOnClickListener(v -> {
            NavController controller=Navigation.findNavController(v);
            controller.navigate(R.id.action_editFragment_to_listFragment);
        });
        binding.dingwei.setOnClickListener(v -> {//定位
            Intent intent = new Intent(requireActivity(), PoiCitySearchActivity.class);
            startActivityForResult(intent, 100);
        });
        typeSlectorInit();
        timeSlectorInit();
        popWindowInit();
    }

    void popWindowInit() {
        Drawable down = ContextCompat.getDrawable(requireContext(), R.drawable.icon_down);
        down.setBounds(0, 0, 25, 25);
        Drawable up = ContextCompat.getDrawable(requireContext(), R.drawable.icon_up);
        up.setBounds(0, 0, 25, 25);

        binding.time.setCompoundDrawables(null, null, up, null);
        binding.type.setCompoundDrawables(null, null, up, null);

        binding.time.setOnClickListener(v -> {

            popupWindow_time.setOutsideTouchable(true);
            popupWindow_time.setFocusable(true);
            popupWindow_time.showAsDropDown(v);


            binding.time.setCompoundDrawables(null, null, down, null);
        });
        binding.type.setOnClickListener(v -> {
            popupWindow_type.setOutsideTouchable(true);
            popupWindow_type.setFocusable(true);
            popupWindow_type.showAsDropDown(v);

            binding.type.setCompoundDrawables(null, null, down, null);
        });
        popupWindow_time.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.time.setCompoundDrawables(null, null, up, null);
            }
        });
        popupWindow_type.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                binding.type.setCompoundDrawables(null, null, up, null);
            }
        });
    }

    void typeSlectorInit() throws NoSuchFieldException, IllegalAccessException {
        //绑定布局，设置控件点击事件
        View popup_view = requireActivity().getLayoutInflater().inflate(R.layout.type_pop, null);
        for (int i = 1; i <= 3; i++) {
            Class<?> d = R.id.class;
            Field field = d.getField("type" + i);
            TextView type = popup_view.findViewById(field.getInt(requireActivity()));
            int finalI = i;
            type.setOnClickListener(v -> {
                Log.d(TAG, "typeSlector: " + finalI);
                activity.setThemeid(finalI);
                switch (finalI) {
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
                popupWindow_type.dismiss();
            });
        }

        popupWindow_type = new PopupWindow(popup_view, binding.type.getMaxWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    void timeSlectorInit() throws NoSuchFieldException, IllegalAccessException {
        Long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat wkFormat = new SimpleDateFormat("EEEE", Locale.CHINA);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        String s = dateFormat.format(calendar.getTime()) + " " + wkFormat.format(calendar.getTime());
        binding.time.setText(s);

        View popup_view = requireActivity().getLayoutInflater().inflate(R.layout.time_pop, null);//获取视图

        for (int i = 1; i <= 7; i++) {//反射通过字符串获取id，设置日期
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_WEEK, i - 1);
            String wk = wkFormat.format(calendar.getTime());
            String day = dateFormat.format(calendar.getTime());
            Log.d(TAG, "timeSlectorInit: " + wk + " " + day);
            Class<?> w = R.id.class;
            Field wfiled = w.getField("time" + i);
            TextView wk_view = popup_view.findViewById(wfiled.getInt(requireActivity()));
            wk_view.setText(wk);//星期几

            Class<?> d = R.id.class;
            Field day_field = d.getField("day" + i);
            TextView day_view = popup_view.findViewById(day_field.getInt(requireActivity()));
            day_view.setText(day);//日期

            int finalI = i;
            String res = wk + " " + day;
            s = sFormat.format(calendar.getTime());
            String finalS = s;
            day_view.setOnClickListener(v -> {
                Log.d(TAG, "timeSlectorInit: " + finalI);
                binding.time.setText(res);
                activity.setPtime(finalS);
            });
            wk_view.setOnClickListener(v -> {
                Log.d(TAG, "timeSlectorInit: " + finalI);
                binding.time.setText(res);
                activity.setPtime(finalS);
            });

        }
        popupWindow_time = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == 100) {
            if (resultCode == 101) {
                String res = data.getStringExtra("descript");
                activity.setLatitude(data.getDoubleExtra("latitude", 0.0));
                activity.setLongitude(data.getDoubleExtra("longitude", 0.0));
                binding.location.setText(res);
                Log.d(TAG, "onActivityResult: " + res);
                flag=true;

            }
        }


    }


}