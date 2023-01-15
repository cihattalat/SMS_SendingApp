package com.example.sms_sending_app.models;

public class GroupModel {

    String title;
    String desc;
    String img_url;

    public GroupModel() {
    }

    public GroupModel(String title, String desc, String img_url) {
        this.title = title;
        this.desc = desc;
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
