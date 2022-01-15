package com.example.werewolfkiller.woveskill;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.werewolfkiller.R;
import com.example.werewolfkiller.modle.Role;
import com.example.werewolfkiller.woveskill.data.Mould;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAd extends RecyclerView.Adapter<RecyclerAd.MyViewHolder> {
    List<Role> roles;

    WoveskillViewModle woveskillViewModle;

    public int a;

    public RecyclerAd(Activity activity, WoveskillViewModle woveskillViewModle) {//textlenth：48
        this.woveskillViewModle=woveskillViewModle;
        this.roles= Mould.getMould().roleList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        itemView = layoutInflater.inflate(R.layout.item_roles, parent, false);

        final MyViewHolder holder = new MyViewHolder(itemView);

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roles.get(holder.getAdapterPosition()).getNum()!=0){
                   woveskillViewModle.userSub();
                    roles.get(holder.getAdapterPosition()).setNum(roles.get(holder.getAdapterPosition()).getNum()-1);
                    holder.num.setText(String.valueOf(roles.get(holder.getAdapterPosition()).getNum()));
                }
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woveskillViewModle.userAdd();
                roles.get(holder.getAdapterPosition()).setNum(roles.get(holder.getAdapterPosition()).getNum()+1);
                holder.num.setText(String.valueOf(roles.get(holder.getAdapterPosition()).getNum()));
            }
        });
        return holder;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.view.setBackgroundDrawable(creatDrawable(roles.get(position).getColor()));
        holder.imageView.setImageResource(roles.get(position).getImg());
        holder.name.setText(roles.get(position).getName());
        holder.content.setText(roles.get(position).getAbility());
        holder.num.setText(String.valueOf(roles.get(position).getNum()));


    }


    @Override
    public int getItemCount() {
        return roles.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {//数据库表格字段与控键关联
        TextView name, sub, add, content, num;
        View view;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.content = itemView.findViewById(R.id.context);
            this.add = itemView.findViewById(R.id.add);
            this.sub = itemView.findViewById(R.id.sub);
            this.num = itemView.findViewById(R.id.num);
            this.imageView = itemView.findViewById(R.id.img);
            this.view = itemView.findViewById(R.id.bk_view);

        }
    }

    public GradientDrawable creatDrawable(String color) {
        int radius = 20;
        int strokeWidth = 5;
        int strokeColor = Color.parseColor(color);
        int bgColor = Color.parseColor(color);
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(bgColor);
        shape.setCornerRadius(radius);
        shape.setStroke(strokeWidth, strokeColor);

        return shape;
    }
    public void loadConfigure(){
       List<Integer> configure=new ArrayList<>();
       int num=1;
        for (Role role:roles) {
            for (int i = 0; i<role.getNum(); i++) {
                configure.add(num);
            }
            num++;
        }
        woveskillViewModle.setConfigure(configure);

    }


}
