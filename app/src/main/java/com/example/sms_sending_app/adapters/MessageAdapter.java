package com.example.sms_sending_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.models.MessageModel;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageModel> messages;
    private Context context;

    public MessageAdapter(ArrayList<MessageModel> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        holder.title.setText(messages.get(position).getMessageTitle());
        holder.message.setText(messages.get(position).getMessage());

        String broadcastmessageTitle = messages.get(position).getMessageTitle();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("MessageBroadcast");
                intent.putExtra("messageBroadcastItem", broadcastmessageTitle);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_message_item_title);
            message = itemView.findViewById(R.id.tv_message_item_message);

        }
    }
}
