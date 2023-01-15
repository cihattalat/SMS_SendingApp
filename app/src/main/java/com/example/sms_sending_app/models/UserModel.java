package com.example.sms_sending_app.models;

public class UserModel {

    String email;
    String password;
    String uid;

    public UserModel() {
    }

    public UserModel(String email, String password, String uid) {
        this.email = email;
        this.password = password;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
