package com.example.sms_sending_app.models;

public class MessageModel {

    String messageTitle;
    String message;

    public MessageModel() {
    }

    public MessageModel(String messageTitle, String message) {
        this.messageTitle = messageTitle;
        this.message = message;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
