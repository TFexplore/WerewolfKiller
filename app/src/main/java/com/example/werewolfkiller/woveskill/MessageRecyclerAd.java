package com.example.werewolfkiller.woveskill;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.werewolfkiller.R;
import com.example.werewolfkiller.modle.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageRecyclerAd extends RecyclerView.Adapter<MessageRecyclerAd.MyViewHolder> {
    public List<Message> messages = new ArrayList<>();

    public MessageRecyclerAd(Activity activity) {//textlenth：48
        messages.add(new Message("听风：", "000001", "进入房间"));
        messages.add(new Message("爱吃...：", "000001", "进入房间"));
        messages.add(new Message("听风：", "000001", "入座1号位"));

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        itemView = layoutInflater.inflate(R.layout.cell_messge, parent, false);

        final MyViewHolder holder = new MyViewHolder(itemView);

        return holder;
    }

    public void setRoles(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(messages.get(position).getName());
        holder.content.setText(messages.get(position).getMsg());

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {//数据库表格字段与控键关联
        TextView name, content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.message_name);
            this.content = itemView.findViewById(R.id.message_content);
        }
    }


}
