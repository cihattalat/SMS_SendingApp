package com.example.sms_sending_app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.Tools;
import com.example.sms_sending_app.adapters.MessageAdapter;
import com.example.sms_sending_app.adapters.VerticalGroupAdapter;
import com.example.sms_sending_app.models.GroupModel;
import com.example.sms_sending_app.models.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class nav_send_message extends Fragment {

    ArrayList<MessageModel> messageModelArrayList;
    MessageAdapter messageAdapter;
    RecyclerView messageItemRv;

    ArrayList<GroupModel> groupModelArrayList;
    VerticalGroupAdapter groupAdapter;
    RecyclerView groupsItemRv;

    TextView selectedGroupTitle, selectedMessageTitle;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_nav_send_message, container, false);
        selectedGroupTitle = root.findViewById(R.id.tv_sendMessage_selectedGroup);
        selectedMessageTitle = root.findViewById(R.id.tv_sendMessage_selectedMessage);
        firebaseAuth = FirebaseAuth.getInstance();
        messageItemRv = root.findViewById(R.id.rv_sendMessage_allMessages);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        llm.setReverseLayout(false);
        messageItemRv.setLayoutManager(llm);

        messageModelArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageModelArrayList,getActivity());
        messageItemRv.setAdapter(messageAdapter);

        groupsItemRv = root.findViewById(R.id.rv_sendMessage_allGroups);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager.setReverseLayout(false);
        groupsItemRv.setLayoutManager(linearLayoutManager);

        groupModelArrayList = new ArrayList<>();
        groupAdapter = new VerticalGroupAdapter(groupModelArrayList,getActivity());
        groupsItemRv.setAdapter(groupAdapter);


        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                selectedGroupTitle.setText(intent.getStringExtra("memberGroup"));
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,new IntentFilter("AddMemberGroup"));

        BroadcastReceiver messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                selectedMessageTitle.setText(intent.getStringExtra("messageBroadcastItem"));
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver,new IntentFilter("MessageBroadcast"));

        getCloudData();

        return root;
    }
    public void getCloudData(){
        db.collection("users").document(firebaseAuth.getUid()).collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document: task.getResult()){
                    MessageModel model = document.toObject(MessageModel.class);
                    messageModelArrayList.add(model);
                    messageAdapter.notifyDataSetChanged();
                }
            }
        });
        db.collection("users").document(firebaseAuth.getUid()).collection("groups").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        GroupModel model = document.toObject(GroupModel.class);
                        groupModelArrayList.add(model);
                        groupAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Tools.showMessage("Veri Çekilemedi. Bir hata oluştu");
                }
            }
        });
    }
}