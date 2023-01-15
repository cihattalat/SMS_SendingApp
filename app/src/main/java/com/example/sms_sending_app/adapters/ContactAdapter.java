package com.example.sms_sending_app.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.models.ContactModel;
import com.example.sms_sending_app.models.GroupModel;
import com.example.sms_sending_app.models.MessageModel;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ArrayList<ContactModel> contacts;
    private Context context;

    public ContactAdapter(ArrayList<ContactModel> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_horizantal,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        ContactModel contact = contacts.get(position);
        holder.title.setText(contacts.get(position).getName());
        holder.desc.setText(contacts.get(position).getPhone());
        if(contact.getPhoto()!=null){
            Glide.with(context).load(contacts.get(position).getPhoto()).into(holder.img);
        }
        else {
            holder.img.setImageResource(R.mipmap.ic_launcher_round);
        }


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_group_item_horizantal);
            title = itemView.findViewById(R.id.tv_gih_title);
            desc = itemView.findViewById(R.id.tv_gih_desc);

        }
    }
}
