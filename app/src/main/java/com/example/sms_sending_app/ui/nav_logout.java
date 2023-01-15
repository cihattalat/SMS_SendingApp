package com.example.sms_sending_app.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sms_sending_app.R;
import com.example.sms_sending_app.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;

public class nav_logout extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(this.getActivity(), SplashActivity.class));
        return inflater.inflate(R.layout.fragment_nav_logout, container, false);
    }
}