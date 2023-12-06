package com.example.mygrouproom;

public class Messages {
    String message;
    String senderId;
    long timeStamp;

    public Messages(long time, String msgKey, String message) {
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
