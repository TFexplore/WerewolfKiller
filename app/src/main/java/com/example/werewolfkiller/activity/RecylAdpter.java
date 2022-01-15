package com.example.werewolfkiller.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.model.LatLng;
import com.example.werewolfkiller.R;
import com.example.werewolfkiller.activity.poisearch.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class RecylAdpter extends RecyclerView.Adapter<RecylAdpter.MyViewHolder> {
    public List<com.example.werewolfkiller.activity.Activity> activityList = new ArrayList<>();


    Activity activity;
    LatLng latLng;
    OnMsgReturned onMsgReturned;
    public RecylAdpter(Activity activity) {
        this.activity=activity;
       /*activityList.add(new com.example.wolveskill.activity.Activity(
                "狼人杀#南山加勒比周五12人萌新场",
                "南岸区-崇文路-12号",
                "12/10 星期五",
                "<5km",
                12,
                6));
        activityList.add(new com.example.wolveskill.activity.Activity(
                "狼人杀#南山加勒比周五12人萌新场",
                "南岸区-崇文路-12号",
                "12/10 星期五",
                "<5km",
                12,
                6));
        activityList.add(new com.example.wolveskill.activity.Activity(
                "狼人杀#南山加勒比周五12人萌新场",
                "南岸区-崇文路-12号",
                "12/10 星期五",
                "<5km",
                12,
                6));

        */

    }
    public void setActivityList(List<com.example.werewolfkiller.activity.Activity> activityList) {
        this.activityList = activityList;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        itemView = layoutInflater.inflate(R.layout.card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Log.d("111111111111111", "onBindViewHolder: "+latLng.latitude+""+latLng.longitude);
        //Log.d("111111111111111", "onBindViewHolder: "+activityList.get(position).getLatlng().latitude+""+activityList.get(position).getLatlng().longitude);
        double distans= Utils.getDistance2(latLng,activityList.get(position).getLatlng());
        String s;

        if (distans>1000){
          s=""+distans/1000+"km";
        }else s=""+distans+"m";


        activityList.get(position).setDistence(s);
        holder.title.setText(activityList.get(position).getTitle());
        holder.location.setText(activityList.get(position).getLocal());
        holder.time.setText(activityList.get(position).getPtime());
        holder.num.setText(String.valueOf(activityList.get(position).cnum));
        holder.distance.setText(s);//activityList.get(position).getDistence()
        Drawable time= ContextCompat.getDrawable(activity,R.drawable.icon_time);
        time.setBounds(-2,0,30,30);
        holder.time.setCompoundDrawables(time,null,null,null);
        Drawable location= ContextCompat.getDrawable(activity,R.drawable.icon_location);
        location.setBounds(-2,0,30,30);
        holder.location.setCompoundDrawables(location,null,null,null);
        Drawable team= ContextCompat.getDrawable(activity,R.drawable.icon_team);
        team.setBounds(-2,0,30,30);
        holder.num.setCompoundDrawables(team,null,null,null);

        holder.itemView.setOnClickListener(v->{
            onMsgReturned.onReturned(new Gson().toJson(activityList.get(position)));
            //EventBus.getDefault().post(new MessageEvent(1,new Gson().toJson(activityList.get(position))));
        });
    }


    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {//数据库表格字段与控键关联
        TextView title, location,time,distance,num;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title_card);
            this.location = itemView.findViewById(R.id.location_card);
            this.time = itemView.findViewById(R.id.time_card);
            this.distance = itemView.findViewById(R.id.distance_card);
            this.num = itemView.findViewById(R.id.num_card);
        }
    }
    public static interface OnMsgReturned{
        void onReturned(String msg);
        void onFaied();
    }


}