package com.example.mygrouproom;

public class Users {
    String uid;
    String name;
    String email;
    String imageUri;
    String status;
    String language;
    long regisTimeStamp;

    public Users() {
    }

    public Users(String uid, String name, String email, String imageUri, String status, String language, long regisTimeStamp) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.status = status;
        this.language = language;
        this.regisTimeStamp = regisTimeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getRegisTimeStamp() {
        return regisTimeStamp;
    }

    public void setRegisTimeStamp(long regisTimeStamp) {
        this.regisTimeStamp = regisTimeStamp;
    }
}