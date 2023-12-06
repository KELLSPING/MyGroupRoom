package com.example.mygrouproom;

public class Messages {
    String message;
    String senderId;
    String timeStamp;

    public Messages(String time, String msgKey, String message) {
        this.message = message;
        this.senderId = msgKey;
        this.timeStamp = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
