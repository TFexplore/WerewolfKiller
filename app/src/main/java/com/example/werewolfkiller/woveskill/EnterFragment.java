package com.example.werewolfkiller.woveskill;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.werewolfkiller.MainActivity;
import com.example.werewolfkiller.MessageEvent;
import com.example.werewolfkiller.MyViewModle;
import com.example.werewolfkiller.R;
import com.example.werewolfkiller.databinding.FragmentEnterBinding;
import com.example.werewolfkiller.woveskill.operate.Operator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentEnterBinding binding;

    private String inputConent;
    private TextView[] textViews;
    MyViewModle myViewModle;

    public EnterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterFragment newInstance(String param1, String param2) {
        EnterFragment fragment = new EnterFragment();
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
        binding = FragmentEnterBinding.inflate(getLayoutInflater());
        //inflater.inflate(R.layout.fragment_enter, container, false);
        MainActivity.setWindowStatusBarColor(getActivity(), getActivity().getResources().getColor(R.color.enter));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myViewModle=new ViewModelProvider(requireActivity()).get(MyViewModle.class);
        init();
    }

    private void init() {
        textViews = new TextView[6];
        textViews[0] = requireView().findViewById(R.id.enter_text1);
        textViews[1] = requireView().findViewById(R.id.enter_text2);
        textViews[2] = requireView().findViewById(R.id.enter_text3);
        textViews[3] = requireView().findViewById(R.id.enter_text4);
        textViews[4] = requireView().findViewById(R.id.enter_text5);
        textViews[5] = requireView().findViewById(R.id.enter_text6);


        binding.enterEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                inputConent = binding.enterEdittext.getText().toString();
                for (int i = 0; i < 6; i++) {
                    if (i < inputConent.length()) {
                        textViews[i].setText(String.valueOf(inputConent.charAt(i)));
                    } else textViews[i].setText("");
                }
                if (inputConent.length() >= 6) {
                    Log.d(TAG, "afterTextChanged: inputcomplete " + "enter");
                    InputMethodManager manager = (InputMethodManager) requireContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        //隐藏软盘
                        manager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    Operator.getINSTANS().setActivity(requireActivity());
                    Operator.getINSTANS().getRoom(inputConent);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
            if (event.getCode()==0){
            NavController controller = Navigation.findNavController(requireView());
            controller.navigate(R.id.action_enterFragment_to_cellFragment);
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}