package com.example.werewolfkiller.woveskill;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.werewolfkiller.R;

public class DialogNv extends DialogFragment {

    public static DialogNv INSTANCE =new DialogNv();
    private OnMsgResultListener msgResultListener;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v=inflater.inflate(R.layout.dialog_nv,null);
        dialogInit(v);


        return v;
    }
    private DialogNv(){
        bundle=new Bundle();
    }
    void dialogInit(View v){

    }

    public static DialogNv getINSTANCE() {
        return INSTANCE;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setMsgResultListener(OnMsgResultListener msgResultListener) {
        this.msgResultListener = msgResultListener;
    }

    public interface OnMsgResultListener{
        void onResult(int result);
    }
}
