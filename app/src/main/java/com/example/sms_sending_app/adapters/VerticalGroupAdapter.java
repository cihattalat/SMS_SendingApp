package com.example.sms_sending_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.models.GroupModel;
import com.example.sms_sending_app.models.MessageModel;

import java.util.ArrayList;

public class VerticalGroupAdapter extends RecyclerView.Adapter<VerticalGroupAdapter.ViewHolder> {

    private ArrayList<GroupModel> groups;
    private Context context;

    public VerticalGroupAdapter(ArrayList<GroupModel> groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @NonNull
    @Override
    public VerticalGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerticalGroupAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_vertical,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalGroupAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        holder.title.setText(groups.get(position).getTitle());
        holder.desc.setText(groups.get(position).getDesc());
        Glide.with(context).load(groups.get(position).getImg_url()).into(holder.img);
        String broadcastTitle = groups.get(position).getTitle();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("AddMemberGroup");
                intent.putExtra("memberGroup", broadcastTitle);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_giv_img);
            title = itemView.findViewById(R.id.tv_giv_title);
            desc = itemView.findViewById(R.id.tv_giv_desc);

        }
    }
}
