package com.example.werewolfkiller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.werewolfkiller.databinding.FragmentPersonBinding;
import com.example.werewolfkiller.modle.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentPersonBinding binding;
   MyViewModle myViewModle;
    User user;

    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
        binding=FragmentPersonBinding.inflate(inflater);
        return binding.getRoot();//inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myViewModle=new ViewModelProvider(requireActivity()).get(MyViewModle.class);

        try {
            dataInit();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        touchInit();
        binding.back.setOnClickListener(v->{
            NavController navController= Navigation.findNavController(v);
            navController.navigate(R.id.action_personFragment_to_masterFragment);
        });
    }
    void  dataInit() throws NoSuchFieldException, IllegalAccessException {
        Gson gson=new Gson();
        user=gson.fromJson(gson.toJson(myViewModle.getUser()),User.class);
        Class<?> d = R.mipmap.class;
        Field imgId = null;
        imgId = d.getField("icon_" + user.getUavatar());
        binding.imageAvatar.setImageResource(imgId.getInt(requireActivity()));
        binding.editName.setText(user.getUname());
        binding.textUtel.setText(user.getUtel());
        if (user.getUqq()!=null){
            binding.editQq.setText(user.getUqq());
        }
        if (user.getUwechat()!=null){
            binding.editWx.setText(user.getUwechat());
        }
        Log.d(TAG, "dataInit: "+user.getUwechat()+user.getUqq());
        binding.editAge.setText(""+user.getUage());
        binding.editSex.setText(user.getUsex());
        binding.personEditText.setGravity(Gravity.TOP);
        binding.personEditText.setText(user.getUpersonality());

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class<?> d = R.mipmap.class;
                    Field imgId = null;
                    imgId = d.getField("icon_" + 11);
                    InputStream in=getResources().openRawResource(imgId.getInt(requireActivity()));
                    Bitmap bitmap= BitmapFactory.decodeStream(in);//
                    //Bitmap blur=ImagUtils.toBlur(bitmap,2);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.viewAvatarbk.setBackground(BlurImageview.BlurImages(bitmap,requireActivity().getApplicationContext()));
                        }
                    });
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        //thread.start();
    }
    void touchInit(){
        binding.editSave.setOnClickListener(v->{
        user.setUname(binding.editName.getText().toString());
        String string=binding.editAge.getText().toString(); Integer age=0;
        if (!string.equals("null")){
            age= Integer.parseInt(string);
        }
        user.setUage(age);
        user.setUqq(binding.editQq.getText().toString());
        user.setUwechat(binding.editWx.getText().toString());
        user.setUpersonality(binding.personEditText.getText().toString());
        user.setUsex(binding.editSex.getText().toString());
            Gson gson=new Gson();
            Log.d(TAG, "touchInit: "+gson.toJson(user));
            MyOkhttp.getINSTANCE().updataUser(user,myViewModle.getToken());
            MyOkhttp.getINSTANCE().getCall().enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireActivity().getApplicationContext(),"请检查网落后重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d(TAG, "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res=response.body().string();
                    Log.d(TAG, "onResponse: "+res);
                    Gson gson = new Gson();
                    Map rmap = gson.fromJson(res, Map.class);
                    myViewModle.setUser(user);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ((Double) rmap.get("code") == 200) {
                                Toast.makeText(requireActivity().getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                                myViewModle.setUser(user);
                            }else if ((Double) rmap.get("code") == 500) {
                                Toast.makeText(requireActivity().getApplicationContext(),"faied:"+ rmap.get("message"), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            });

        });
    }
}