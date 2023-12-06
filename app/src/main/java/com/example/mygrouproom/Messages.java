package com.example.mygrouproom;

public class Messages {
    String name;
    String senderId;
    String dataTime;
    String msgKey;
    String message;

    public Messages() {
    }

    public Messages(String name, String senderId, String dataTime, String msgKey, String message) {
        this.name = name;
        this.senderId = senderId;
        this.dataTime = dataTime;
        this.msgKey = msgKey;
        this.message = message;
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

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
