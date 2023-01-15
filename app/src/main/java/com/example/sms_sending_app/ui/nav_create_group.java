package com.example.sms_sending_app.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.Tools;
import com.example.sms_sending_app.adapters.CreateGroupAdapter;
import com.example.sms_sending_app.adapters.MessageAdapter;
import com.example.sms_sending_app.models.GroupModel;
import com.example.sms_sending_app.models.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

public class nav_create_group extends Fragment {

    ImageView selectedImage;
    Button createGroupButton;
    TextView et_title, et_desc;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference = storage.getReference();

    ArrayList<GroupModel> groupModelArrayList;
    CreateGroupAdapter groupAdapter;
    RecyclerView groupsItemRv;


    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nav_create_group, container, false);
        createGroupButton = (Button) root.findViewById(R.id.button_createGroup);
        et_title = (EditText) root.findViewById(R.id.et_groupName);
        et_desc = (EditText) root.findViewById(R.id.et_groupDesc);
        selectedImage = (ImageView) root.findViewById(R.id.iv_selected_image);
        firebaseAuth = FirebaseAuth.getInstance();

        groupsItemRv = root.findViewById(R.id.rv_allGroups);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        llm.setReverseLayout(false);
        groupsItemRv.setLayoutManager(llm);

        groupModelArrayList = new ArrayList<>();
        groupAdapter = new CreateGroupAdapter(groupModelArrayList,getActivity());
        groupsItemRv.setAdapter(groupAdapter);

        getCloudData();


        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData(firebaseAuth.getUid());
            }
        });



        return root;
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
    String res = "";
    public void uploadData(String uid) {
        if(filePath !=null){
            StorageReference ref = reference.child("images/"+uid+"/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    res = uri.toString();
                                    if(et_title.getText().toString().isEmpty()&&et_desc.getText().toString().isEmpty()&&res == ""){
                                        Tools.showMessage("Lütfen tüm alanları doldurunuz.");
                                    }
                                    else {
                                        GroupModel model = new GroupModel(et_title.getText().toString(),et_desc.getText().toString(),res);
                                        db.collection("users").document(uid).collection("groups").document().set(model)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        et_title.setText("");
                                                        et_desc.setText("");
                                                        Tools.showMessage("Başarıyla kaydedildi");
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    });
        }
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null&& data.getData()!=null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                selectedImage.setImageBitmap(bitmap);
            }
            catch (Exception e){
                Log.d("Error: ",e.toString());
            }
        }
    }
}