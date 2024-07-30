package com.example.chatappli.model;

import com.google.firebase.Timestamp;

public class Usermodel {
    public static Usermodel userModel;
    private  String phno;
    private  String username;
    private com.google.firebase.Timestamp createdTimestamp;
    private String userId;




    public Usermodel() {
    }

    public Usermodel(String phno, String username, com.google.firebase.Timestamp createdTimestamp,String userId) {
        this.username=username;
        this.phno = phno;
        this.createdTimestamp=createdTimestamp;
        this.userId=userId;


    }




    public String getPhno() {
        return phno;
    }


    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public com.google.firebase.Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Usermodel getUserModel() {
        return userModel;
    }

    public static void setUserModel(Usermodel userModel) {
        Usermodel.userModel = userModel;
    }


}
