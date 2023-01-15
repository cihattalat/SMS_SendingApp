package com.example.sms_sending_app.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.Tools;
import com.example.sms_sending_app.adapters.ContactAdapter;
import com.example.sms_sending_app.adapters.CreateGroupAdapter;
import com.example.sms_sending_app.adapters.VerticalGroupAdapter;
import com.example.sms_sending_app.models.ContactModel;
import com.example.sms_sending_app.models.GroupModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class nav_add_member extends Fragment {


    ArrayList<GroupModel> groupModelArrayList;
    VerticalGroupAdapter groupAdapter;
    RecyclerView groupsItemRv;

    ArrayList<ContactModel> contactList;
    ContactAdapter contactAdapter;
    RecyclerView contactItemRv;

    TextView selectedItemTitle;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_nav_add_member, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        selectedItemTitle = (TextView) root.findViewById(R.id.tv_add_member_selected_groups);

        groupsItemRv = root.findViewById(R.id.rv_addMember_groups);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        llm.setReverseLayout(false);
        groupsItemRv.setLayoutManager(llm);

        groupModelArrayList = new ArrayList<>();
        groupAdapter = new VerticalGroupAdapter(groupModelArrayList,getActivity());
        groupsItemRv.setAdapter(groupAdapter);

        contactItemRv = root.findViewById(R.id.rv_contacts);
        LinearLayoutManager llmContact = new LinearLayoutManager(getActivity());
        llmContact.setOrientation(RecyclerView.VERTICAL);
        llmContact.setReverseLayout(false);
        contactItemRv.setLayoutManager(llmContact);
        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList,getActivity());
        contactItemRv.setAdapter(contactAdapter);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                      selectedItemTitle.setText(intent.getStringExtra("memberGroup"));
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,new IntentFilter("AddMemberGroup"));

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_CONTACTS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                getContacts();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Tools.showMessage("İzin hatası");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();


        getCloudData();

        return root;
    }

    private void getContacts() {
        Cursor phone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null);
        while (phone.moveToNext()){
            @SuppressLint("Range") String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range") String phoneUrl = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            ContactModel model = new ContactModel(name, phoneNumber, phoneUrl);
            contactList.add(model);
            contactAdapter.notifyDataSetChanged();
        }
    }

    public void getCloudData(){
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