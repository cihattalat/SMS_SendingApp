package com.example.sms_sending_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    Button buttonSignup, buttonLogin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Tools.context = getApplicationContext();

        getSupportActionBar().hide();

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, SignupActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        });



        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                }
                catch (Exception e){
                    Log.d("Error: ",e.toString());
                }
                finally {
                    if(firebaseAuth.getCurrentUser()!=null){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }

                }
            }
        };
        thread.start();
    }
}