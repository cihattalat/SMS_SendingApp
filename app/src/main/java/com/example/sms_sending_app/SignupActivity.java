package com.example.sms_sending_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.sms_sending_app.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {


    EditText emailController, passwordController;
    Button buttonSignup, buttonLogin;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        emailController = (EditText) findViewById(R.id.et_signup_email);
        passwordController = (EditText) findViewById(R.id.et_signup_password);
        buttonLogin = (Button) findViewById(R.id.button_signup_login);
        buttonSignup = (Button) findViewById(R.id.button_signup_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSignup);
        progressBar.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                registerUser();
            }

        });
    }

    public void registerUser() {

        String email = emailController.getText().toString();
        String password = passwordController.getText().toString();
        if(email.isEmpty()||password.isEmpty()){
            Tools.showMessage("Tüm alanlar doldurulmalıdır.");
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if(task.isSuccessful()){
                        String uid = task.getResult().getUser().getUid();
                        UserModel model = new UserModel(email,password,uid);
                        db.collection("users").document(uid).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Tools.showMessage("Kayıt Başarılı");
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            }
                        });

                    }
                    else {
                        Tools.showMessage("Beklenmedik hata oluştu. Lütfen daha sonra tekrar deneyiniz.");
                    }
                }
            });
        }
    }
}