package com.example.sms_sending_app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.Tools;
import com.example.sms_sending_app.adapters.MessageAdapter;
import com.example.sms_sending_app.models.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class nav_create_message extends Fragment {

    TextView messageName, message;
    Button createMessageButton;
    FirebaseAuth firebaseAuth;

    ArrayList<MessageModel> messageModelArrayList;
    MessageAdapter messageAdapter;
    RecyclerView messageItemRv;


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_nav_create_message, container, false);
        messageName = (EditText) root.findViewById(R.id.et_message_name);
        message = (EditText) root.findViewById(R.id.et_message);
        createMessageButton = (Button) root.findViewById(R.id.button_create_message);
        firebaseAuth = FirebaseAuth.getInstance();

        messageItemRv = root.findViewById(R.id.rv_allMessages);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        llm.setReverseLayout(false);
        messageItemRv.setLayoutManager(llm);

        messageModelArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageModelArrayList,getActivity());
        messageItemRv.setAdapter(messageAdapter);

        getCloudData();



        createMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMessage(firebaseAuth.getUid());
            }
        });
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
    }

    public void createMessage(String uid) {
        String msgName = messageName.getText().toString();
        String msg = message.getText().toString();
        if(msg.isEmpty()&&msgName.isEmpty()){
            Tools.showMessage("Lütfen tüm alanları dolduruğunuzdan emin olunuz.");
        }
        else {
            MessageModel model = new MessageModel(msgName,msg);
            db.collection("users").document(uid).collection("messages").document().set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Tools.showMessage("Başarıyla kaydedildi");
                    messageName.setText("");
                    message.setText("");

                }
            });
        }
    }
}