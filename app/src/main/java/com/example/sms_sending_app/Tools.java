package com.example.sms_sending_app;

import android.widget.Toast;
import android.content.Context;

public class Tools {
    public static Context context;
    public static void showMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
