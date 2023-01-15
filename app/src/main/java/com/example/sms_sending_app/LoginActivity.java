package com.example.sms_sending_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailController, passwordController;
    Button buttonSignup, buttonLogin;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        emailController = (EditText) findViewById(R.id.et_login_mail);
        passwordController = (EditText) findViewById(R.id.et_login_password);
        buttonLogin = (Button) findViewById(R.id.button_login_login);
        buttonSignup = (Button) findViewById(R.id.button_login_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loginUser();
            }
        });
    }

    public void loginUser() {
        String email = emailController.getText().toString();
        String password = passwordController.getText().toString();
        if(email.isEmpty()||password.isEmpty()){
            Tools.showMessage("Tüm alanlar doldurulmalıdır.");
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if(task.isSuccessful()){
                        Tools.showMessage("Giriş Başarılı");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else {
                        Tools.showMessage("Kullanıcı adı ya da şifreniz yanlış. Lütfen tekrar deneyiniz.");
                    }
                }
            });
        }
    }
}