package com.example.werewolfkiller.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.werewolfkiller.R;
import com.example.werewolfkiller.databinding.FragmentDetailBinding;
import com.google.gson.Gson;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentDetailBinding binding;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
        binding=FragmentDetailBinding.inflate(inflater);
        return binding.getRoot();//inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataInit();
    }
    void dataInit(){
        String res=getArguments().getString("res");
        Log.d(TAG, "onActivityCreated: "+res);
        Activity activity=new Gson().fromJson(res,Activity.class);
        binding.titleCard.setText(activity.getTitle());
        binding.locationCard.setText(activity.getLocal());
        binding.timeCard.setText(activity.getPtime());
        binding.numCard.setText(String.valueOf(activity.cnum));
        binding.distanceCard.setText(activity.getDistence());
        Drawable time= ContextCompat.getDrawable(requireActivity(), R.drawable.icon_time);
        time.setBounds(-2,0,30,30);
        binding.timeCard.setCompoundDrawables(time,null,null,null);
        Drawable location= ContextCompat.getDrawable(requireActivity(),R.drawable.icon_location);
        location.setBounds(-2,0,30,30);
        binding.locationCard.setCompoundDrawables(location,null,null,null);
        Drawable team= ContextCompat.getDrawable(requireActivity(),R.drawable.icon_team);
        team.setBounds(-2,0,30,30);
        binding.numCard.setCompoundDrawables(team,null,null,null);

        binding.content.setText(activity.getContent());

    }


}