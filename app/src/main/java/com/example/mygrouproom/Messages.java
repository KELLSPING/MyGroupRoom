package com.example.mygrouproom;

public class Messages {
    String name;
    String senderId;
    long sendMsgTimeStamp;
    String message;
    String imageUri;

    public Messages() {
    }

    public Messages(String name, String senderId, long sendMsgTimeStamp, String message, String imageUri) {
        this.name = name;
        this.senderId = senderId;
        this.sendMsgTimeStamp = sendMsgTimeStamp;
        this.message = message;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getSendMsgTimeStamp() {
        return sendMsgTimeStamp;
    }

    public void setSendMsgTimeStamp(long sendMsgTimeStamp) {
        this.sendMsgTimeStamp = sendMsgTimeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
